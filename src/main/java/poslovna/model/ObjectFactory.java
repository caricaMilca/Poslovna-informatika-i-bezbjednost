package poslovna.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	  public ObjectFactory() {
	    }

	    public AnalitikaIzvoda createAnalitikaIzvoda() {
	        return new AnalitikaIzvoda();
	    }

/*	    public Valuta createValuta() {
	    	return new Valuta();
	    }*/
	    
	    public StavkePlacanja createStavkePlacanja(){
	    	return new StavkePlacanja();
	    }
}
