
package com.example.donisaurus.ecomplaint.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ModelLike {

    @SerializedName("id_like")
    @Expose
    private Integer idLike;
    @SerializedName("id_post")
    @Expose
    private Integer idPost;
    @SerializedName("id_user_liked")
    @Expose
    private Integer idUserLiked;

    /**
     * 
     * @return
     *     The idLike
     */
    public Integer getIdLike() {
        return idLike;
    }

    /**
     * 
     * @param idLike
     *     The id_like
     */
    public void setIdLike(Integer idLike) {
        this.idLike = idLike;
    }

    /**
     * 
     * @return
     *     The idPost
     */
    public Integer getIdPost() {
        return idPost;
    }

    /**
     * 
     * @param idPost
     *     The id_post
     */
    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    /**
     * 
     * @return
     *     The idUserLiked
     */
    public Integer getIdUserLiked() {
        return idUserLiked;
    }

    /**
     * 
     * @param idUserLiked
     *     The id_user_liked
     */
    public void setIdUserLiked(Integer idUserLiked) {
        this.idUserLiked = idUserLiked;
    }

}
