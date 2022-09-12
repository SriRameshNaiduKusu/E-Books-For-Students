package in.cwt.ebooksforstudents.model;

public class Update {
     String updatelink;
     String updatetext;
     String updatetitle;
     String updateversion;
     String positive;


    public Update(){

    }



    public Update(String updatelink, String updatetext, String updatetitle, String updateversion, String positive){


        this.updatelink = updatelink;
        this.updatetext = updatetext;
        this.updatetitle = updatetitle;
        this.updateversion = updateversion;
        this.positive = positive;

    }




    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }
    public String getUpdatelink() {
        return updatelink;
    }

    public void setUpdatelink(String updatelink) {
        this.updatelink = updatelink;
    }

    public String getUpdatetext() {
        return updatetext;
    }

    public void setUpdatetext(String updatetext) {
        this.updatetext = updatetext;
    }

    public String getUpdatetitle() {
        return updatetitle;
    }

    public void setUpdatetitle(String updatetitle) {
        this.updatetitle = updatetitle;
    }

    public String getUpdateversion() {
        return updateversion;
    }

    public void setUpdateversion(String updateversion) {
        this.updateversion = updateversion;
    }
}
