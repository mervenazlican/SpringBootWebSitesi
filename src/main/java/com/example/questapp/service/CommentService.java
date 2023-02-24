package com.example.questapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.questapp.entities.Comment;
import com.example.questapp.entities.Post;
import com.example.questapp.entities.User;
import com.example.questapp.repository.CommentRepository;
import com.example.questapp.requests.CommentCreateRequest;
import com.example.questapp.requests.CommentUpdateRequest;

@Service
public class CommentService {
	private CommentRepository commentRepository;
	private UserService userService;
	private PostService postService;
	
	public CommentService(CommentRepository commentRepository, UserService userService,PostService postService) {
		this.commentRepository=commentRepository;
		this.userService=userService;
		this.postService=postService;
	}

	public List<Comment> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {
		if(userId.isPresent() && postId.isPresent()) {
			return commentRepository.findByUserIdAndPostId(userId.get(),postId.get());
		}
		else if(userId.isPresent()) {
			return commentRepository.findByUserId(userId.get());
		}
		else if(postId.isPresent()) {
			return commentRepository.findByPostId(postId.get());
		}else {
			return commentRepository.findAll();
		}
	}

	public Comment getOneCommentById(Long commentId) {
		return commentRepository.findById(commentId).orElse(null);
	}

	public Comment createOneComment(CommentCreateRequest createRequest) {
		User user = userService.getOneUserById(createRequest.getUserId());
		Post post = postService.getOnePostById(createRequest.getPostId());
		if(user != null && post !=null ) {
			Comment commentToSave = new Comment();
			commentToSave.setId(createRequest.getId());
			commentToSave.setPost(post);
			commentToSave.setUser(user);
			commentToSave.setText(createRequest.getText());
			return commentRepository.save(commentToSave);
		}else {
			return null;
		}
	}

	public Comment updateOneCommentById(Long commentId,CommentUpdateRequest updateRequest) {
		Optional<Comment> comment= commentRepository.findById(commentId);
		if(comment.isPresent()) {
			Comment commentToUpdate = comment.get();
			commentToUpdate.setText(updateRequest.getText());
			return commentRepository.save(commentToUpdate);
		}else
			return null;
	}

	public void deleteOneCommentById(Long commentId) {
		commentRepository.deleteById(commentId);
		
	}
}
