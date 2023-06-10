package com.example.hellogodfather;

import com.example.hellogodfather.model.Post;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.hellogodfather.model.Post;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PostModelTest {

    /**
     * Test whether the new post instance is empty
     * Since the constructor will initialize the likeList by
     * adding a head, the size shouldn't be 0
     */
    @Test
    public void testPeopleLikeThePostNonEmpty() {
        ArrayList<String> usersLikeThePost = new ArrayList<>();
        Post post = new Post("-MmYo340PkufW3P1XDna", "jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", "William",
                "@agntecarter: i?m emotional", "", "2021-10-22 01:05:05",
                "2021-10-22 01:05:05", usersLikeThePost);
        assertEquals(1, post.countLikes());

    }

    /**
     * Test setter methods by reflection
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testSetterMethods() throws NoSuchFieldException, IllegalAccessException {
        Post post = new Post();

        Field postID = post.getClass().getDeclaredField("postID");
        Field authorID = post.getClass().getDeclaredField("authorID");
        Field userName = post.getClass().getDeclaredField("userName");
        Field postContent = post.getClass().getDeclaredField("postContent");
        Field tags = post.getClass().getDeclaredField("tags");
        Field postTime = post.getClass().getDeclaredField("postTime");
        Field userIconPath = post.getClass().getDeclaredField("userIconPath");
        Field likeThePost = post.getClass().getDeclaredField("usersLikeThePost");

        postID.setAccessible(true);
        authorID.setAccessible(true);
        userName.setAccessible(true);
        postContent.setAccessible(true);
        tags.setAccessible(true);
        postTime.setAccessible(true);
        userIconPath.setAccessible(true);
        likeThePost.setAccessible(true);

        post.setPostID("-MmYo340PkufW3P1XDna");
        post.setAuthorID("jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2");
        post.setUserName("William");
        post.setTags("");
        ArrayList<String> userLikeListTest = new ArrayList<>();
        post.setUsersLikeThePost(userLikeListTest);
        post.setPostContent("hahaha");
        post.setPostTime("2021-10-22 01:05:05");
        post.setUserIconPath("PRkVVernG8WFbaDq7PRrgzSm8Tg1_profile");

        assertEquals("-MmYo340PkufW3P1XDna", postID.get(post));
        assertEquals("jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", authorID.get(post));
        assertEquals("William", userName.get(post));
        assertEquals("hahaha", postContent.get(post));
        assertEquals("", tags.get(post));
        assertEquals("2021-10-22 01:05:05", postTime.get(post));
        userLikeListTest.add("#");
        assertEquals(userLikeListTest, likeThePost.get(post));
    }

    /**
     * Test of getter methods
     */

    @Test
    public void testGetterMethods() {
        //Given
        ArrayList<String> usersLikeThePost = new ArrayList<>();
        usersLikeThePost.add("-MmYo340PkufW3P1XDna");

        Post post = new Post("-MmYo340PkufW3P1XDna", "jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", "William",
                "hahaha", "", "2021-10-22 01:05:05",
                "PRkVVernG8WFbaDq7PRrgzSm8Tg1_profile", usersLikeThePost);
        post.setPostID("-MmYo340PkufW3P1XDna");

        //When
        Post postTest = new Post();
        postTest.setPostID("-MmYo340PkufW3P1XDna");
        postTest.setAuthorID("jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2");
        postTest.setUserName("William");
        postTest.setTags("");
        ArrayList<String> userLikeListTest = new ArrayList<>();
        userLikeListTest.add("-MmYo340PkufW3P1XDna");
        postTest.setUsersLikeThePost(userLikeListTest);
        postTest.setPostContent("hahaha");
        postTest.setPostTime("2021-10-22 01:05:05");
        postTest.setUserIconPath("PRkVVernG8WFbaDq7PRrgzSm8Tg1_profile");


        // Then
        assertEquals("-MmYo340PkufW3P1XDna", post.getPostID());
        assertEquals("jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", post.getAuthorID());
        assertEquals("William", post.getUserName());
        assertEquals("hahaha", post.getPostContent());
        assertEquals("", post.getTags());
        assertEquals("2021-10-22 01:05:05", post.getPostTime());
        assertEquals(2, post.getUsersLikeThePost().size());
    }

    @Test
    public void testTags() {
        ArrayList<String> usersLikeThePost = new ArrayList<>();
        usersLikeThePost.add("-MmYo340PkufW3P1XDna");
        Post post = new Post("-MmYo340PkufW3P1XDna", "jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", "William",
                "#Yeah! hahaha", "", "2021-10-22 01:05:05",
                "PRkVVernG8WFbaDq7PRrgzSm8Tg1_profile", usersLikeThePost);

        assertEquals("#yeah", post.tagsFromContent(post.getPostContent()));
    }








}
