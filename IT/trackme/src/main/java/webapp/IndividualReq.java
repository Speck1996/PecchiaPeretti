package webapp;

import login.AuthenticationUtils;
import manager.IndividualRequestManager;
import model.Attribute;
import model.IndividualEntity;
import model.View;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Named
public class IndividualReq {

    @EJB
    IndividualRequestManager requestManager;

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    private String name;
    private String identifier;
    private String idType;
    private String description;
    private String[] view;
    private String[] attr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getView() {
        return view;
    }

    public void setView(String[] view) {
        this.view = view;
    }

    public String[] getAttr() {
        return attr;
    }

    public void setAttr(String[] attr) {
        this.attr = attr;
    }

    public void processRequest() {
        System.out.println(print());

        String username = AuthenticationUtils.getUsernameByCookies(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());

        String taxcode = null;

        if(idType.equals("taxcode"))
            taxcode = identifier;
        else {
            TypedQuery<IndividualEntity> query = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class);
            query.setParameter("username", identifier);

            IndividualEntity result = query.getSingleResult();
            if(result == null)
                return;

            taxcode = result.getTaxcode();
        }

        short views = View.getNumericViews(view);
        short attributes = Attribute.getNumericAttributes(attr);

        requestManager.newIndividualRequest(username, taxcode, name, null, views, attributes);

        System.out.println("Request completed");


    }

    public String print() {
        String s = "id: " + identifier + " idtype: " + idType + " descr: " + description + "view: [";
        if(view != null) {
            for(String v: view)
                s += v + " ";
        }
        s += "] attr: [";
        if(attr != null) {
            for(String a: attr)
                s += a + " ";
        }
        s += "]";

        return s;
    }
}
