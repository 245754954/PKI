package nudt.web.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class ContactAttributeMapperJSON implements AttributesMapper {
    @Override
    public Object mapFromAttributes(Attributes attributes) throws NamingException {

        NamingEnumeration<String> ids = attributes.getIDs();
        JSONObject json = new JSONObject();

        while (ids.hasMore())
        {
            String id = ids.next();
            json.put(id,attributes.get(id).get());
        }
        return json.toString();
    }
}
