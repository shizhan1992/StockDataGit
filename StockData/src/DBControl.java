import java.net.UnknownHostException;
import java.util.Date;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class DBControl {
	static Mongo mongo = null;
	static Morphia morphia = null;
	static Datastore ds = null;
	static DB db = null;
	public DBControl() {
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongo.getDB("xueqiu");
		morphia = new Morphia();
		morphia.map(Topic.class);
	}
	
	public void close(){
		mongo.close();
	}
	public static void SaveCount(String code, int count){
		DBObject stock = new BasicDBObject();
		stock.put("symbol", code);
		stock.put("count", count);
		db.getCollection(code).save(stock);
	}
	public static int GetCount(String code){
		DBObject object = new BasicDBObject();
		object.put("symbol", code);
		DBObject stock = db.getCollection(code).findOne(object);
		if(stock != null)
			return (Integer)stock.get("count");
		else 
			return 0;
	}
	public static void SaveTopic(String code, Topic topic){
		DBObject DbObj = morphia.toDBObject(topic);
		db.getCollection(code).save(DbObj);
	}

	public static String GetText(String code, Date start, Date end) {
		String alltext = "";
		BasicDBObject keys = new BasicDBObject();
		keys.put("created_at", new BasicDBObject("$gte", start).append("$lte", end));
		DBCursor cursor = db.getCollection(code).find(keys);
//		long x = db.getCollection(code).count(keys);		

		while(cursor.hasNext()){
			for(int i =0; i<1000 ;i++){
				String text = DeleteNoise(String.valueOf(cursor.next().get("text")));
				alltext += String.valueOf(cursor.next().get("text"));
			}
		}
		System.out.println(alltext);
		return alltext;
	}

	private static String DeleteNoise(String text) {
		String noHtmlContent = text.replaceAll("<[^>]*>","").replaceAll("&nbsp", "");
		return noHtmlContent;
	}
	
}