package in.sweetcherry.pswebservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "prestashop")
public class PSWebServiceProperties {
	
	private String ws_base_url;
	private String ws_cookie_key;
	private String ws_access_key;
	private int    id_allocated_order_state;
	private int    id_shipped_order_state;
	private int    id_preparation_order_state;
	private int    id_delivered_order_state;
	private int    id_attempted_order_state;
	private int    id_deliveryboy_profile;
	private int    id_administrator_profile;
	private int    id_superadmin_profile;
	
	private long[] id_awaiting_delivery_order_states;
	
	private long[] id_admin_profiles;
	
	public String getWs_base_url() {
		return ws_base_url;
	}
	public void setWs_base_url(String ws_base_url) {
		this.ws_base_url = ws_base_url;
	}
	public String getWs_cookie_key() {
		return ws_cookie_key;
	}
	public void setWs_cookie_key(String ws_cookie_key) {
		this.ws_cookie_key = ws_cookie_key;
	}
	public String getWs_access_key() {
		return ws_access_key;
	}
	public void setWs_access_key(String ws_access_key) {
		this.ws_access_key = ws_access_key;
	}
	
	public int getId_allocated_order_state() {
		return id_allocated_order_state;
	}
	public void setId_allocated_order_state(int id_allocated_order_state) {
		this.id_allocated_order_state = id_allocated_order_state;
	}
	
	public int getId_shipped_order_state() {
		return id_shipped_order_state;
	}
	public void setId_shipped_order_state(int id_shipped_order_state) {
		this.id_shipped_order_state = id_shipped_order_state;
	}
	public int getId_preparation_order_state() {
		return id_preparation_order_state;
	}
	public void setId_preparation_order_state(int id_preparation_order_state) {
		this.id_preparation_order_state = id_preparation_order_state;
	}
	public int getId_deliveryboy_profile() {
		return id_deliveryboy_profile;
	}
	public void setId_deliveryboy_profile(int id_deliveryboy_profile) {
		this.id_deliveryboy_profile = id_deliveryboy_profile;
	}
	public int getId_delivered_order_state() {
		return id_delivered_order_state;
	}
	public void setId_delivered_order_state(int id_delivered_order_state) {
		this.id_delivered_order_state = id_delivered_order_state;
	}
	public int getId_attempted_order_state() {
		return id_attempted_order_state;
	}
	public void setId_attempted_order_state(int id_attempted_order_state) {
		this.id_attempted_order_state = id_attempted_order_state;
	}
	
	public long[] getId_awaiting_delivery_order_states() {
		return id_awaiting_delivery_order_states;
	}
	public void setId_awaiting_delivery_order_states(long[] id_awaiting_delivery_order_states) {
		this.id_awaiting_delivery_order_states = id_awaiting_delivery_order_states;
	}
	
	public int getId_administrator_profile() {
		return id_administrator_profile;
	}
	public void setId_administrator_profile(int id_administrator_profile) {
		this.id_administrator_profile = id_administrator_profile;
	}
	public int getId_superadmin_profile() {
		return id_superadmin_profile;
	}
	public void setId_superadmin_profile(int id_superadmin_profile) {
		this.id_superadmin_profile = id_superadmin_profile;
	}
	
	public long[] getId_admin_profiles() {
		return id_admin_profiles;
	}
	public void setId_admin_profiles(long[] id_admin_profiles) {
		this.id_admin_profiles = id_admin_profiles;
	}
	@Override
	public String toString() {
		return "PSWebServiceProperties [ws_base_url=" + ws_base_url + ", ws_cookie_key=" + ws_cookie_key + ", ws_access_key="
				+ ws_access_key + "]";
	}
	
	

}
