package land.server.api;

import java.util.List;

import land.server.model.Student;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

@Api(name = "student",
version = "v1",
description = "Student API",
namespace = @ApiNamespace(ownerDomain = "2lnd", ownerName = "2lnd", packagePath=""))

public class StudentAPI {
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@ApiMethod(name ="create", httpMethod = HttpMethod.POST)
	public void create(Student data) {
		Entity entity = new Entity("Student");
		entity.setProperty("name", data.getName());
		datastore.put(entity);
	}
	
	@ApiMethod(name = "list", httpMethod = HttpMethod.GET)
	public List<Entity> list() {
		Query query = new Query("Student");
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for (Entity entity : entities) {
			entity.setProperty("encodedKey", KeyFactory.keyToString(entity.getKey()));
		}
		return entities;
	}
	
	@ApiMethod(name ="delete", httpMethod = HttpMethod.GET)
	public void delete(@Named("encodedKey") String encodedKey) {
		Key key = KeyFactory.stringToKey(encodedKey);
		datastore.delete(key);
	}
	
	@ApiMethod(name ="update", httpMethod = HttpMethod.POST)
	public void update(Student data) throws EntityNotFoundException {
		Key key = KeyFactory.stringToKey(data.getEncodedKey());
		Entity entity = datastore.get(key);
		entity.setProperty("name", data.getName());
		datastore.put(entity);
	}
}
