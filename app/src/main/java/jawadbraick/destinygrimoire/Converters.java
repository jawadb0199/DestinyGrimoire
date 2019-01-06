package jawadbraick.destinygrimoire;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.Charset;

public class Converters{
    private static JsonParser parser = new JsonParser();

    @TypeConverter
    public static JsonObject fromJson(byte[] bytes){
//        String str = new String(bytes, Charset.forName("UTF-8"));
        String s = new String(bytes);
        return parser.parse(s.substring(0, s.length()-1)).getAsJsonObject();
    }
    @TypeConverter
    public static byte[] toJson(JsonObject json){
        return json.toString().getBytes(Charset.forName("UTF-8"));
    }

//    @TypeConverter
//    public static JsonObject fromJson(String str){
//        return parser.parse(str).getAsJsonObject();
//    }
//    @TypeConverter
//    public static String toJson(JsonObject json){
//        return json.toString();
//    }

}
