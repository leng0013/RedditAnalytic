package logic;

import common.ValidationException;
import dal.CommentDAL;
import entity.Comment;
import entity.Post;
import entity.RedditAccount;
import entity.Subreddit;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.function.ObjIntConsumer;

public class CommentLogic extends GenericLogic<Comment, CommentDAL> {

    public static final String UNIQUEID = "unique_id";
    public static final String ID = "id";
    public static final String TEXT = "text";
    public static final String CREATED = "created";
    public static final String POINTS = "points";
    public static final String REPLYS = "replys";
    public static final String ISREPLY = "is_reply";
    public static final String REDDIT_ACCOUNT_ID = "reddit_account_id";
    public static final String POST_ID = "post_id";


    public CommentLogic() {
        super(new CommentDAL());
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "text", "created", "points", "replys", "is_reply", "unique_id", "post_id", "reddit_account_id");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, TEXT, CREATED, POINTS, REPLYS, ISREPLY, UNIQUEID, POST_ID, REDDIT_ACCOUNT_ID);
    }

    @Override
    public List<?> extractDataAsList(Comment comment) {
        return Arrays.asList(comment.getId(), comment.getText(), comment.getCreated(), comment.getPoints(), comment.getReplys(), comment.getIsReply(), comment.getUniqueId(), comment.getPostId(), comment.getRedditAccountId());
    }

    @Override
    public Comment createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        Comment entity = new Comment();
        RedditAccount ra = new RedditAccount();
        Post post = new Post();
        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        ObjIntConsumer<String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };
        String reddit_account_id = parameterMap.get(REDDIT_ACCOUNT_ID)[0];
        String post_id = parameterMap.get(POST_ID)[0];
        String uniqueid = parameterMap.get(UNIQUEID)[0];
        String points = parameterMap.get(POINTS)[0];
        String text = parameterMap.get(TEXT)[0];
        String created = parameterMap.get(CREATED)[0];
        String replys = parameterMap.get(REPLYS)[0];
        String is_reply = parameterMap.get(ISREPLY)[0];

        //validate the data
        validator.accept(uniqueid, 10);
        validator.accept(text, 10000);
        validator.accept(is_reply, 1);
        ra.setId(Integer.parseInt(reddit_account_id));
        post.setId(Integer.parseInt(post_id));


        //set values on entity
        entity.setRedditAccountId(ra);
        entity.setPostId(post);
        entity.setCreated(new Date(created));
        entity.setUniqueId(uniqueid);
        entity.setText(text);
        entity.setPoints(Integer.parseInt(points));
        entity.setReplys(Integer.parseInt(replys));
        entity.setIsReply(Boolean.parseBoolean(is_reply));


        return entity;
    }

    @Override
    public List<Comment> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Comment getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public Comment getCommentWithUniqueId(String uniqueId) {
        return get(() -> getCommentWithUniqueId(uniqueId));
    }

    public List<Comment> getCommentsWithText(String text) {
        return get(() -> getCommentsWithText(text));
    }

    public List<Comment> getCommentsWithCreated(Date created) {
        return get(() -> getCommentsWithCreated(created));
    }

    public List<Comment> getCommentsWithPoints(int points) {
        return get(() -> getCommentsWithPoints(points));
    }

    public List<Comment> getCommentsWithReplys(int replys) {
        return get(() -> getCommentsWithReplys(replys));
    }

    public List<Comment> getCommentsWithIsReply(boolean isReply) {
        return get(() -> getCommentsWithIsReply(isReply));
    }
}
