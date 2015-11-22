package com.models;

import java.sql.SQLException;
import java.util.*;

import com.models.utility.Constants;
import com.models.dataaccess.DataAccess;

import com.models.entity.Resources;
import com.models.entity.Services;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.network.Network;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.model.identity.Tenant;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WebsiteHandlers {

    Services service = new Services();
    OSClient os = OSFactory.builder()
            .endpoint("http://localhost:5000/v2.0")
            .credentials("admin","sample")
            .tenantName("admin")
            .authenticate();

    /** Define constructor **/
    public WebsiteHandlers(){
        System.out.println(">>>>>>>>>>>>> Initiate website handler <<<<<<<<<<<<<<<");
    }

    public WebsiteHandlers(Services service) {
        System.out.println(">>>>>>>>>>>>> Initiate website handler with services <<<<<<<<<<<<<<<");
        this.service = service;
    }

    //create openstack instance
    public void process(int id, String servicename) {
        String imageId = null;
        String serverName = null;
        DataAccess db = new DataAccess();
        JSONObject result =  new JSONObject();

        try {
            Image webImage = os.compute().images().get("3fe4b8c0-d90e-47c2-be10-81f14b83e71b");
            Image dbImage = os.compute().images().get("3fe4b8c0-d90e-47c2-be10-81f14b83e71b");

            Network fixed_network = os.networking().network().get("e219602b-dad5-4c85-b571-4059c186a2f8");
            Network public_network = os.networking().network().get("319a8b77-086c-4b20-84f3-400861472f89");

            ArrayList<String> networks = new ArrayList<String>();
            networks.add(fixed_network.getId());
            networks.add(public_network.getId());
            result.put("id", "");
            result.put("serviceid", service.getServiceid());
            result.put("servicename", service.getServicename());
            result.put("uid", service.getUid());
            result.put("datecreated",service.getDatecreated());
            result.put("resourcetype",Constants.RESOURCE_TYPE);
            result.put("fixednetwork", Constants.FIXED_NETWORK);
            result.put("floatingnetwork", Constants.FLOATING_NETWORK);
            result.put("securitygroup", Constants.SECURITY_GROUP);
            result.put("keypair", Constants.KEYPAIR);
            result.put("port", Constants.FIXED_ETH_NETWORK);
            service = db.getServiceByName(servicename);
            if(service != null) {
                //Creating a small website
                if(service.getServicetype().equalsIgnoreCase("smallWebsite")) {
                    imageId = webImage.getId();
                    Resources resource = new Resources();
                    serverName = service.getUid()+ ":smallWebsite:" + service.getServiceid();
                    //FloatingIP ip = os.compute().floatingIps().allocateIP("public");
                    String resource_status = createVM(imageId, serverName, networks);
                    resource.setDatecreated(service.getDatecreated());
                    resource.setServiceid(service.getServiceid());
                    resource.setStatus(resource_status);
                    result.put("fixedip", "");
                    result.put("floatingip", "");
                    resource.setJson(result.toString());
                    if(resource_status.equals(Constants.CREATED)){
                        db.addResource(Constants.RESOURCE_TYPE , resource, service);
                    }
                } else { //Creating a big website
                    result.remove("fixedip");
                    result.remove("floatingip");
                    imageId = webImage.getId();
                    Resources resource = new Resources();
                    serverName = service.getUid()+":bigWebsite:"+service.getServiceid();
                    String resource_status = createVM(imageId, serverName, networks);
                    resource.setDatecreated(service.getDatecreated());
                    resource.setServiceid(service.getServiceid());
                    resource.setStatus(resource_status);
                    result.put("fixedip", "");
                    result.put("floatingip", "");
                    resource.setJson(result.toString());
                    db.addResource("instance" , resource, service);

                    result.remove("fixedip");
                    result.remove("floatingip");
                    resource = new Resources();
                    imageId = webImage.getId();
                    serverName = service.getUid()+":bigWebsite:"+service.getServiceid();
                    resource_status = createVM(imageId, serverName, networks);
                    resource.setDatecreated(service.getDatecreated());
                    resource.setServiceid(service.getServiceid());
                    resource.setStatus(resource_status);
                    result.put("fixedip", "");
                    result.put("floatingip", "");
                    resource.setJson(result.toString());
                    db.addResource("instance" , resource, service);

                    result.remove("fixedip");
                    result.remove("floatingip");
                    resource = new Resources();
                    imageId = dbImage.getId();
                    serverName = service.getUid()+":bigWebsite:"+service.getServiceid();
                    resource_status = createVM(imageId, serverName, networks);
                    resource.setDatecreated(service.getDatecreated());
                    resource.setServiceid(service.getServiceid());
                    resource.setStatus(resource_status);
                    result.put("fixedip", "");
                    result.put("floatingip", "");
                    resource.setJson(result.toString());
                    db.addResource("instance" , resource, service);
                }
                //delete(id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create VM
     * @param imageId
     * @param serverName
     * @param networks
     * @return
     */
    public String createVM(String imageId, String serverName, ArrayList<String> networks) {
        String server_status = Constants.CREATED;
        Flavor flavor = os.compute().flavors().get("1");
        ServerCreate sc = Builders.server()
                .name(serverName)
                .flavor(flavor.getId())
                .image(imageId)
                .networks(networks)
                .addSecurityGroup("sample")
                .addPersonality("/etc/motd", "Welcome to the new VM! Restricted access only")
                .build();
        Server server = os.compute().servers().boot(sc);
        return server_status;
    }

    //Create tenant
    public String createTenant(String name, String description) {
        System.out.println(">>>>>>>>> Create tenant <<<<<<<<<<<<<<<<<");
        String tenant_status = Constants.CREATED;
        Tenant tenant = os.identity().tenants()
                .create(Builders.tenant().name(name).description(description).build());
        return tenant_status;
    }
}