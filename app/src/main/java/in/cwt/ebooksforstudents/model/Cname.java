package in.cwt.ebooksforstudents.model;

public  class Cname {
    public String cname;
    public String id;
    public String cpic;


    public Cname(){

    }




    public Cname(String cname , String id, String cpic){
        this.cname = cname;
        this.id = id;
        this.cpic = cpic;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpic() {
        return cpic;
    }

    public void setCpic(String cpic) {
        this.cpic = cpic;
    }
}

