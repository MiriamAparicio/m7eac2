package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alex
 */
@Local
public interface RateLocal {
    public List<RestClass> getRatedRest();
    public void setRatedRest(List<RestClass> ratedRest);
}
