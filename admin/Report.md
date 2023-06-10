# [Team Name] Report

The following is a report template to help your team successfully provide all the details necessary for your report in a structured and organised manner. Please give a straightforward and concise report that best demonstrates your project. Note that a good report will give a better impression of your project to the reviewers.

*Here are some tips to write a good report:*

* *Try to summarise and list the `bullet points` of your project as much as possible rather than give long, tedious paragraphs that mix up everything together.*

* *Try to create `diagrams` instead of text descriptions, which are more straightforward and explanatory.*

* *Try to make your report `well structured`, which is easier for the reviewers to capture the necessary information.*

*We give instructions enclosed in square brackets [...] and examples for each sections to demonstrate what are expected for your project report.*

*Please remove the instructions or examples in `italic` in your final report.*

## Table of Contents

1. [Team Members and Roles](#team-members-and-roles)
2. [Conflict Resolution Protocol](#conflict-resolution-protocol)
2. [Application Description](#application-description)
3. [Application UML](#application-uml)
3. [Application Design and Decisions](#application-design-and-decisions)
4. [Summary of Known Errors and Bugs](#summary-of-known-errors-and-bugs)
5. [Testing Summary](#testing-summary)
6. [Implemented Features](#implemented-features)
7. [Team Meetings](#team-meetings)

## Team Members and Roles

note taker, documentation checker, user input, team leader, help page code, morale builder, tricky android code, app state, graphics rendering

| UID | Name | Role |
| :--- | :----: | ---: |
| u7211980 | Peng Zhao | [role] |
| u6814863 | Ruizheng Shen | [role] |
| u7205634 | Xiao Xu | [role] |
| u7110012 | Yucheng Zhao | [role] |


## Conflict Resolution Protocol

*[Write a well defined protocol your team can use to handle conflicts. That is, if your group has problems, what is the procedure for reaching consensus or solving a problem? (If you choose to make this an external document, link to it here)]*
*1. Demonstrate opinion to everyone, and find out a solution to the conflict through meeting.*
*2. Respect everyone.*

## Application Description

*[What is your application, what does it do? Include photos or diagrams if necessary]*

*Here is a pet specific social media application example*
*HelloGodfather is a lightweight social media application designed for the communication within the fandom of a movie - The GodFather. It is a classical 70's move and therefore Our targeting users are the people at middle age
In order to enrich the user experience in such community, we simplify the UI and interaction functions as much as possible to make the application easy to use and sufficient to achieve the communication purpose. 
Users can sign-up through the email authentication. Their personal information will be remembered such that they can sign-in automatically without inputting their name/password. The simple login process saves the users' time.
Furthermore, it provides the fans with a vintage UI style with navy and orange, which represent the  aggressive, dangerous and noble nature of the godfather. In addition,we designed a cardView that can be smoothly scrolled 
up/down to obtain a quick overview of posts. The design improve users' reading experience and save their reading time.  Texting is always the simplest but the most efficient way for users at this age group. Users can compose a post 
post and send them into the community. The post will be uploaded onto our online storage(firebase) such that the users will see his post immediately in the posts' view. The amplified view of each card is provided for users to interact with others.
If users like or want to share the post with the community, they can repost or add to their favourite for convenience. Our app also supports for searching. For people who is interested in some topics or people who likes all posts composed
by an author, they can search the posts by the post tags or the usernames. The high-efficiency searching highlights users' interests, saving their time cost for obtaining information. Finally, the customizable user profile offers functionality  
to edit their user name and upload the icons they prefer to present. Users could view their personal statics as well. For example, they can view the number of posts which they like and how many likes they received. Geometrical information is provided 
for people to view their current location.*


**Application Use Cases and or Examples**

*[Provide use cases and examples of people using your application. Who are the target users of your application? How do the users use your application?]*

*Tony is middle-aged man who likes the GodFather, he wants to know more about the movie and share his film review with others*
1. *Tony opens the app and wants to view others' opinions about the Godfather*
2. *He finds a post by the tag that he's interested in and clicks the card to read more*
3. *After reading, he has emotional resonance with the author, he like the post and repost it*
4. *A few days later, he found a nice image and wants to change his icon*
5. *Entering into the profile page, he finds a number of people like his post*
6. *He is encouraged by the statics and decide to compose his own film review*


## Application UML

[DaoUML](UML/DaoUML.png)
<br>
[ModelUML](UML/DaoUML.png)
<br>
[ParserUML](UML/DaoUML.png)

## Application Design and Decisions

*Please give clear and concise descriptions for each subsections of this part. It would be better to list all the concrete items for each subsection and give no more than `5` concise, crucial reasons of your design. Here is an example for the subsection `Data Structures`:*

*I used the following data structures in my project:*

1. *Red-Black Tree*

    * *Objective: It is used for storing all the post records in the app and providing a faster searching speed for searching post record by comparable key.*

    * *Locations: RBTree.java and RBTreeWithList.java*

    * *Reasons:*

        * *As we are using firebase to store all the posts records. Waiting all the posts retrieved from firebase is too time consuming, so we come out with an idea that, while starting the app, all the posts will be retrieved and stored in a data structure before the app provides user the operating interface. Considering most of the post search requirement are from the search activity(e.g: search all the posts created by an user with username ‘xxx’), we decided that using a binary search tree was reasonable. As binary search tree has bad performance on the worst case(O(n)), AVL Tree need too much structure adjustment while inserting data, we decided that Red-Black tree will be suitable for post searching(Time complexity on searching, insertion and deletion are all O(logn)). *


2. ...

3. ...

**Data Structures**

*[What data structures did your team utilise? Where and why?]*

**Design Patterns**

*[What design patterns did your team utilise? Where and why?]*

*I use the following design pattern in my project:*
1.  *DAO*
	* *Objective: For every class in model(e.g: User, Post, etc), develop an dao to interact with data storing area(e.g: firebase, database, bespoke)*

    * *Locations: Dao package*

    * *Reasons:*

        * *As we need to do different data accessment operations according to different classes. For example, for the class User, we need to store the id, username, and other user information. For the Post class, we need to store post_id, author’s is, author’s name, post content, tags, etc. So we need to implement different data accessing object to put the methods which interact with database. Also, considering we might have different storing places, we do not want to do this different storing handling operation in the activity class, so we want to abstract the method of data-storing and accessing, so that in activity, we only need to get an instance of DAO and call the data-storing/ data-accessing method according to what we need.*

	* *Other Nodes*
		* *However, we find that the data loading method provided by firebase(the onDataChange method) is asynchronous, if we write the data loading method in DAO, the data loading method will return null(The method has already returned before the onDataChange get the retrieved data). In this app, due to time limitation, we move the data loading methods to activities. To deal with the asynchronous problem, we might need another onComplete listener and a callback method to make the method hang on the waiting status, and only return the result after the data is retrieved.*


2.  *Singleton*
    * *Objective: For the dao class, it is better to have only one dao instance in the activity.*

    * *Locations: Dao package*

    * *Reasons:*

		* *For every data storing and accessing operation, we need only one connection to the database accordingly. Thus, we want to ensure that, every data storing and accessing operation will only create one class instance in the activity, otherwise, there might be conflict on database connection. So for all the dao classes, we implement it in singleton case and provide a method to get the instance of dao to make sure only one instance is existed.*


**Grammars**

*Search Engine*
<br> *Token Types* <br>
\AND, OR, USERNAME, TAG\
<br> *Production Rules* <br>
<br>
\<query>     ::=  \<usernames> ; <tags> | <usernames> | <tags>
<br>
\<usernames> ::=  \<username> | <username> , <usernames>
<br>
\<tags>      ::=  \<tag> | <tag> , <tags>
<br>
\<username>  ::=  \<at> <[a-zA-z0-9_]>
<br>
\<tag>       ::=  \<pound> <[a-zA-z0-9_]>
<br>

Examples:
<br>
Valid query -- "@novak, @paul_; #_Wimbledon, #Melbourne, #Novak20"
<br>
Invalid query -- "////", "@/", "#.", "@novak; @paul"
<br>
Partially valid query -- "@novak; //"
<br>

*[How do you design the grammar? What are the advantages of your designs?]*
Between tags and usernames, there exists 'AND' logic separated by ";". Between tags, there exists 'OR' logic (same for usernames). If the first parts of the query tag conform to first parts of the existing tag in database, we can still get the corresponding results. These designs make users search tags or usernames both individually and together.

**Tokenizer and Parsers**

*[Where do you use tokenisers and parsers? How are they built? What are the advantages of the designs?]*
We use tokenizers and parsers in search page. The current search engine of this app is designed for searching by tags and usernames.
<br>
1. For tags, we set "@" as the predetermined value, while "#" as the predetermined value for username.
2. For tags, the engine accepts both lowercase and uppercase query to match related posts.
3. For usernames, the engine only accepts accurate query. Otherwise, we won't get the results.

**Surpise Item**

*[If you implement the surprise item, explain how your solution addresses the surprise task. What decisions do your team make in addressing the problem?]*

**Other**

*[What other design decisions have you made which you feel are relevant? Feel free to separate these into their own subheadings.]*

## Summary of Known Errors and Bugs

*[Where are the known errors and bugs? What consequences might they lead to?]*

*Here is an example:*

1. *Bug 1:*
 *A space bar (' ') in the sign in email will crash the application.*
2. *Bug 2:*
 *take photo in setting activity sometimes not available.*
3. *Bug 3:*
 *if the nickname has been edited, the post that sent before cannot update the new nickname*
4. *Error 1:*
 *the image reading access time is slow.*
5. *Error 2:*
 *report function is not available.*
 
*List all the known errors and bugs here. If we find bugs/errors that your team do not know of, it shows that your testing is not through.*

## Testing Summary

*[What features have you tested? What is your testing coverage?]*

*Basic features*
1. *Users must be able to login (not necessarily sign up). (up to 3 marks)*
2. *Users must be able to load (from file(s) or Firebase) and view posts (e.g. on a
   timeline activity). (up to 10 marks)*
3. *Users must be able to search for posts by tags (e.g. #COMP2100isTheBest). The
   search functionality must make use of a tokenizer and parser with a grammar of your
   own creation. (up to 3 marks without a tokenizer and parser. Up to 12 marks
   with a tokenizer and parser)*
4. *There must be a data file with at least 1,000 valid data instances. There must be a
   data file that is used to feed the social network app simulating a data stream. For
   example, every x seconds, a new item is read from a file. An item can be a post or
   an action (e.g. like, follow, etc). (up to 5 marks)*

5. *At least one fully implemented data structure taught in this course (e.g., Binary
   Search Tree, Red-Black trees, AVL, etc) for organizing, processing, retrieving and
   storing data. We will also evaluate your choice and use of data structures on your
   project (not only trees, but also other data structures such as arrays, lists, maps).*
6. *Your app shall implement at least two design patterns covered in the course.*


*User Interactivity*
1. *The ability to micro-interact with 'posts' (e.g. like, report, etc.) [stored in-memory].
   (easy)*
2. *The ability to repost a message from another user (similar to 'retweet' on Twitter)
   [stored in-memory]. (easy)*
   
*Greater Data Usage, Handling and Sophistication*
1. *User profile activity containing a media file (image, animation (e.g. gif), video). (easy)*
2. *Use GPS information (see the demo presented by our tutors. For example, your app
   may use the latitude/longitude to show posts). (easy)*
3. *User statistics. Provide users with the ability to see a report of total views, total
   followers, total posts, total likes, in a graphical manner. (medium)*
4. *5. Deletion method of either a Red-Black Tree and or AVL tree data structure. The
   deletion of nodes must serve a purpose within your application (e.g. deleting posts).
   (hard)*
   
*Firebase Integration*
1. *Use Firebase to implement user Authentication/Authorisation. (easy)*
2. *Use Firebase to persist all data used in your app (this item replace the requirement to retrieve data from a local file) (medium)*
3. *Using Firebase or another remote database to store user posts and having a user’s
   timeline update as the remote database is updated without restarting the
   application. E.g. User A makes a post, user B on a separate instance of the
   application sees user A’s post appear on their timeline without restarting their
   application. (very hard)*

## Team Meetings

*Here is an example:*

- *[Team Meeting 1](TeamMeeting/TeamMeeting1.md)*
- *[Team Meeting 2](TeamMeeting/TeamMeeting2.md)*
- *[Team Meeting 3](TeamMeeting/TeamMeeting3.md)*
- *[Team Meeting 4](TeamMeeting/TeamMeeting4.md)*
- *[Team Meeting 5](TeamMeeting/TeamMeeting5.md)*
- *[Team Meeting 6](TeamMeeting/TeamMeeting6.md)*
- *[Team Meeting 7](TeamMeeting/TeamMeeting7.md)*
- *[Team Meeting 8](TeamMeeting/TeamMeeting8.md)*
- *[Team Meeting 9](TeamMeeting/TeamMeeting9.md)*

*Either write your meeting minutes here or link to documents that contain them. There must be at least 3 team meetings.*
