package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import javax.ejb.Local;

/**
 *
 * @author Miriam
 */
@Local
public interface ValidateRestBeanLocal {
    
    public Boolean isValidFileImageName(String RestName, String fileImageName);
    
}
