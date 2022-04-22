package com.bence.mate.models;

import com.bence.mate.resources.MessageResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.ws.rs.core.Link;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Getter
    @Setter
    @JsonIgnore
    private int number;

    @Getter
    @Setter
    @JsonIgnore
    private int size;

    @Getter
    @Setter
    @JsonIgnore
    private boolean isPreviousPageAvailable;

    @Getter
    @Setter
    @JsonIgnore
    private boolean isNextPageAvailable;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    @JsonIgnoreProperties({"uriBuilder", "type", "rels", "title"})
    @InjectLinks({
            @InjectLink(resource = MessageResource.class,
                    rel = "self"),
            @InjectLink(
                    resource = MessageResource.class,
                    method = "getMessage",
                    bindings = @Binding(name = "pathId", value = "${instance.id}"),
                    rel = "get")
    })
    private List<Link> links;
}
