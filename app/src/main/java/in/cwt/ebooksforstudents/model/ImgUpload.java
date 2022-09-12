package in.cwt.ebooksforstudents.model;

public class ImgUpload {
    public String imageLink;
    public String category;
    public String fileName;
    public String thumblink;
    public String state;
    public String cname;
    public String thumbnail;
    public String bookLink;


    public ImgUpload(){
    }



    public ImgUpload(String imageLink, String category, String fileName , String thumblink , String state, String cname, String bookLink
    , String thumbnail) {
        this.imageLink = imageLink;
        this.category = category;
        this.fileName = fileName;
        this.thumblink = thumblink;
        this.state = state;
        this.cname = cname;
        this.bookLink = bookLink;
        this.thumbnail = thumbnail;

    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getimageLink() {
        return imageLink;
    }

    public void setimageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName = fileName;
    }

    public String getthumblink() {
        return thumblink;
    }

    public void setthumblink(String thumblink) {
        this.thumblink = thumblink;
    }



    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }


    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }
}
