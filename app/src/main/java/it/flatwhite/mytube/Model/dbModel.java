package it.flatwhite.mytube.Model;

/**
 * Created by Aaron on 23/11/2017.
 */
public class dbModel {
    // news Strings
    private String title;
    private String desc;
    private String url;



    // tube strings
    private String lineName;
    private String lineStatus;



    // TUBE
    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }




    ///NEWS

    public String getTitle() {return title;}

    public void setTitle(String title) {
        this.title = title;
    }



    public String getDesc() {return desc;}

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public String getUrl() {return url;}

    public void setUrl(String url) {
        this.url = url;
    }







}
