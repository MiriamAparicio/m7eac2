package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import javax.ejb.Stateless;

/**
 *
 * @author Miriam
 */
@Stateless
public class ValidateRestBean implements ValidateRestBeanLocal {

    @Override
    public Boolean isValidFileImageName(String RestName, String fileImageName) {
        
        if (RestName.equalsIgnoreCase(fileImageName)) {
            
            return true;
            
        } else {
            
            return false;
        }
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
