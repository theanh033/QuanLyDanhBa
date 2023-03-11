package theanh.android.quanlydanhba.Object;

import java.util.HashMap;
import java.util.Map;

public class Contacts {
    private String id, name, phone;

    public Contacts() {
    }

    public Contacts(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, Object> map() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("phone", phone);

        return hashMap;
    }
}
