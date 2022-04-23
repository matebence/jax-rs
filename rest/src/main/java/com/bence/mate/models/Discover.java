package com.bence.mate.models;

import com.bence.mate.resources.MessageResource;
import com.bence.mate.resources.ProfileResource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.ws.rs.core.Link;

import java.io.Serializable;
import java.util.List;

@Builder
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class Discover implements Serializable {

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    @JsonIgnoreProperties({ "uriBuilder", "type", "rels", "title"})
    @InjectLinks({
            @InjectLink(resource=ProfileResource.class, rel = "profiles"),
            @InjectLink(resource=MessageResource.class, rel = "messages")
    })
    private List<Link> links;
}
