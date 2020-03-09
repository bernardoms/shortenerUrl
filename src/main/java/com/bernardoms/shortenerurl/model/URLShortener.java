package com.bernardoms.shortenerurl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document(collection = "shorteners")
@JsonIgnoreProperties(ignoreUnknown = true)
public class URLShortener {
		@Id
	    private ObjectId id;
	    private String originalURL;
		private String alias;
		private int redirectCount;
}
