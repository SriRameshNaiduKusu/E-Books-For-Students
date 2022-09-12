package in.cwt.ebooksforstudents.model;

public class Slider {

     String imglink;
     String click;
     String sname;
     String show;

    public Slider(){
    }



    public Slider(String imglink, String click, String sname, String show){
        this.imglink = imglink;
        this.click = click;
        this.sname = sname;
        this.show = show;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
