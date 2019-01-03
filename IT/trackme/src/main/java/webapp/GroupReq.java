package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import model.Sex;
import model.View;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
public class GroupReq {

    @EJB
    GroupRequestManager requestManager;

    private String name;
    private String[] views;
    private String sex;
    private Integer minAge;
    private Integer maxAge;
    private String birthCountry;
    private String location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getViews() {
        return views;
    }

    public void setViews(String[] views) {
        this.views = views;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void processRequest() {
        System.out.println(print());

        String username = AuthenticationUtils.getUsernameByCookies(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());
        //short numericViews = getNumericViews();
        short numericViews = View.getNumericViews(views);
        checkAges();

        Sex sexEnum = getEnumSex();

        if(location != null && location.equals(""))
            location = null;
        if(birthCountry != null && birthCountry.equals(""))
            birthCountry = null;

        //Forward the request to the requests manager
        requestManager.newGroupRequest(username, name, null, numericViews, location, minAge == null ? null : (byte) ((int) minAge),
                maxAge == null ? null : (byte) ((int) maxAge), sexEnum, birthCountry);
        System.out.println("Request forwarded");
    }


    private boolean checkAges() {
        if((maxAge != null && maxAge <= 0) || (minAge != null && maxAge != null && minAge > maxAge))
            return false;

        if(maxAge != null && maxAge > Byte.MAX_VALUE)
            maxAge = (int) Byte.MAX_VALUE;

        if(minAge != null && minAge <= 0)
            minAge = 0;

        return true;
    }

    private Sex getEnumSex() {
        if(sex == null)
            return null;

        switch (sex) {
            case "male":
                return Sex.MALE;
            case "female":
                return Sex.FEMALE;
            default:
                System.out.println("Unexpected sex: " + sex);
                return null;
        }
    }


    public String print() {
        String s = "name: " + name + " views: [";
        if(views != null) {
            for (String v : views)
                s += v + " ";
        }
        s += "] Sex: " + sex + " minAge:" + minAge + " maxAge: " + maxAge + " Country: " + birthCountry + " Location: " + location;

        return s;
    }
}
