
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.*;
import org.xml.sax.SAXException;



public class Parser {
    
    public Parser(String[] args){
        
        File dtd = new File("../../../Groupe_dtd.dtd");
        Source xmlFile = new StreamSource(new File("../../../Test/testSimple.xml"));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        try{
            Schema schema = schemaFactory.newSchema(dtd);
            Validator valide = schema.newValidator();
            valide.validate(xmlFile);
            
        }
        catch(SAXException | IOException e){
            
        }
    }
}
