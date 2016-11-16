package com.mdedu.appservice.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Chapter;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.domainobject.Video;
import com.mdedu.appservice.repository.BookRepository;
import com.mdedu.appservice.repository.ChapterRespository;
import com.mdedu.appservice.repository.VideoRepository;

@RestController
public class BookController {

	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private ChapterRespository chapterRepo;
	
	@Autowired
	private VideoRepository videoRepo;
	
	
	@RequestMapping(path="/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public Response<Collection<Book>> getBooks(@RequestParam(name="subject", required=false) Long subject,
			@RequestParam(name="grade", required=false)Long grade,
			@RequestParam(name="version", required=false)Long version){
		Collection<Book> result;
		if(subject==null&&grade==null && version==null){
			result = (Collection<Book>) bookRepo.findAll();
		}else{
			Example<Book> example = getBookExample(subject, grade, version);
			result = bookRepo.findAll(example );				
		}
		Response<Collection<Book>> response =new Response<Collection<Book>>();
		if(result.isEmpty()){
			response.setStatus("Error");
			response.setErrorMsg("Fail to get books for subject:"+subject+" grade:"+grade+" version:"+version);
		}else{
			response.setStatus("Success");
			response.setData(result);
		}
		return response;
	}
	
	@RequestMapping(path="/book/{bookId}/chapters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )	
	public Response<Collection<Chapter>> getChapters(@PathVariable Long bookId)
	{
		Book b=new Book();
		b.setId(bookId);
		Collection<Chapter> root= chapterRepo.findByBookAndParent(b,null);
		Response<Collection<Chapter>> result= new Response<Collection<Chapter>>();
		root=createChResponse(root);
		if(root!=null){
			result.setStatus("Success");
			result.setData(root);
		}else{
			result.setStatus("Error");
			result.setErrorMsg("Can't find data for "+bookId);
		}
		return result;
	}
	
	@RequestMapping(path="/book/{bookId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )	
	public Response<Collection<Chapter>> getBookAllData(@PathVariable Long bookId)
	{
		Book b=new Book();
		b.setId(bookId);
		Collection<Chapter> root= chapterRepo.findByBookAndParent(b,null);
		Response<Collection<Chapter>> result= new Response<Collection<Chapter>>();		
		root=createChResponseWithVideo(root);
		
		if(root!=null){
			result.setStatus("Success");
			result.setData(root);
		}else{
			result.setStatus("Error");
			result.setErrorMsg("Can't find data for "+bookId);
		}
		return result;
	}
	
	@RequestMapping(path="/chapter/{chId}/videos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )	
	public Response<Collection<Video>> getViedo(@PathVariable Long chId)
	{
		Response<Collection<Video>> response=new Response<Collection<Video>>();
		Chapter ch=new Chapter();
		ch.setId(chId);
		Collection<Video> result = getVideoRes(ch);
		if(result.isEmpty()){
			response.setStatus("Error");
			response.setErrorMsg("failed to query data");
		}else{
			response.setStatus("Success");
			response.setData(result);
		}
		return response;
	}
	@RequestMapping(path="video/playcount",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> updateVideoPlayCount(@RequestBody Video video)
	{
		Response<Void> result=new Response<Void>();
		Video v=videoRepo.findOne(video.getId());
		if(v!=null){
			if(v.getPlayedCount()!=null){
			v.setPlayedCount(v.getPlayedCount()+1);
			}else{
				v.setPlayedCount(1l);
			}
			videoRepo.save(v);
			result.setStatus("Success");
		}else{
			result.setStatus("Error");
			result.setErrorMsg("No video find for:"+video.getId());
		}
		return result;
	}
	private Collection<Video> getVideoRes(Chapter ch) {
		Collection<Video> result = new ArrayList<Video>();
		videoRepo.findByChapter(ch).forEach((v)->{
			Video nv=new Video();
			nv.setDescription(v.getDescription());
			nv.setName(v.getName());
			nv.setPlayedCount(v.getPlayedCount());
			nv.setSeq(v.getSeq());
			nv.setRelatedSeq(v.getRelatedSeq());
			nv.setId(v.getId());
			result.add(nv);
		});
		return result;
	}

	private List<Chapter>  createChResponseWithVideo(Collection<Chapter> root) {
		if(root.isEmpty()){
			return null;
		}
		List<Chapter> result=new ArrayList<Chapter>();
		root.forEach((ch)->{
			ResChapter newCh=new ResChapter();
			newCh.setId(ch.getId());
			newCh.setSeq(ch.getSeq());
			newCh.setTitle(ch.getTitle());
			newCh.setVideos(getVideoRes(ch));
			newCh.setChildren(createChResponseWithVideo(ch.getChildren()));	
		
			result.add(newCh);
		});
		return result;
	}
	
	private List<Chapter>  createChResponse(Collection<Chapter> root) {
		if(root.isEmpty()){
			return null;
		}
		List<Chapter> result=new ArrayList<Chapter>();
		root.forEach((ch)->{
			Chapter newCh=new Chapter();
			newCh.setId(ch.getId());
			newCh.setSeq(ch.getSeq());
			newCh.setTitle(ch.getTitle());
			newCh.setChildren(createChResponse(ch.getChildren()));		
			result.add(newCh);
		});
		return result;
	}
	
	
	private Example<Book> getBookExample(Long subject, Long grade, Long version) {
		Book book=new Book();
		if(subject!=null){
			Subject s=new Subject();
			s.setId(subject);
			book.setSubject(s);
		}
		if(grade!=null){
			Grade g=new Grade();
			g.setId(grade);
			book.setGrade(g);
		}
		if(version!=null){
			Version s=new Version();
			s.setId(version);
			book.setVersion(s);
		}
		return Example.of(book);
	}
	
}
