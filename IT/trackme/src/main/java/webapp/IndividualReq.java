package webapp;

import login.AuthenticationUtils;
import manager.IllegalRequestArgumentException;
import manager.IndividualRequestManager;
import manager.RequestExistsException;
import model.Attribute;
import model.IndividualEntity;
import model.View;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.*;

/**
 * Managed Bean responsible for the forward of a new individual data request
 */
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

    private boolean usernameError;
    private boolean taxcodeError;
    private boolean requestAlreadyExists;

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

    public boolean isUsernameError() {
        return usernameError;
    }

    public void setUsernameError(boolean usernameError) {
        this.usernameError = usernameError;
    }

    public boolean isTaxcodeError() {
        return taxcodeError;
    }

    public void setTaxcodeError(boolean taxcodeError) {
        this.taxcodeError = taxcodeError;
    }

    public boolean isRequestAlreadyExists() {
        return requestAlreadyExists;
    }

    public void setRequestAlreadyExists(boolean requestAlreadyExists) {
        this.requestAlreadyExists = requestAlreadyExists;
    }

    /**
     * Process the data inserted in the form and try to create a new request
     */
    public void processRequest() {
        System.out.println(print());

        String username = AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());

        String taxcode = null;

        if(idType.equals("taxcode"))
            taxcode = identifier;
        else {
            TypedQuery<IndividualEntity> query = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class);
            query.setParameter("username", identifier);

            IndividualEntity result;

            try {
                result = query.getSingleResult();
            } catch (NoResultException e) {
                usernameError = true;
                return;
            }

            if(result == null)
                return;

            taxcode = result.getTaxcode();
        }

        short views = View.getNumericViews(view);
        short attributes = Attribute.getNumericAttributes(attr);

        try {
            requestManager.newIndividualRequest(username, taxcode, name, null, views, attributes);
        } catch (IllegalRequestArgumentException e) {
            taxcodeError = true;
            return;
        } catch (RequestExistsException e) {
            requestAlreadyExists = true;
            return;
        } catch (Exception e) {
            System.out.println("Unexpected exception");
        }

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
