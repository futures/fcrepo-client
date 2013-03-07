package org.fcrepo.client;

import java.io.IOException;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fcrepo.jaxb.responses.access.ObjectDatastreams;
import org.fcrepo.jaxb.responses.access.ObjectProfile;
import org.fcrepo.jaxb.responses.management.DatastreamProfile;

public class FedoraClient {

	private static final String PATH_OBJECT_PROFILE = "/objects/";
	private static final String PATH_OBJECT_DATASTREAMS = "/datstreams/";

	private final HttpClient client = new DefaultHttpClient();
	private final URI fedoraUri;

	private Unmarshaller unmarshaller;

	public FedoraClient(URI fedoraUri) {
		super();
		this.fedoraUri = fedoraUri;
	}

	private Unmarshaller getUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			unmarshaller = JAXBContext.newInstance(ObjectProfile.class, ObjectDatastreams.class, DatastreamProfile.class)
					.createUnmarshaller();
		}
		return unmarshaller;
	}

	public ObjectProfile getObjectProfile(final String id) throws IOException {
		final HttpGet get = new HttpGet(fedoraUri.toASCIIString() + PATH_OBJECT_PROFILE + id);
		final HttpResponse resp = client.execute(get);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Unable to fetch object profile from fedora: " + resp.getStatusLine().getReasonPhrase());
		}
		try {
			ObjectProfile profile = (ObjectProfile) this.getUnmarshaller().unmarshal(resp.getEntity().getContent());
			return profile;
		} catch (JAXBException e) {
			throw new IOException("Unabel to deserialize object profile", e);
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
	}

	public ObjectDatastreams getObjectDatastreams(final String objectId) throws IOException {
		final HttpGet get = new HttpGet(fedoraUri.toASCIIString() + PATH_OBJECT_PROFILE + objectId + PATH_OBJECT_DATASTREAMS);
		final HttpResponse resp = client.execute(get);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Unable to fetch object profile from fedora: " + resp.getStatusLine().getReasonPhrase());
		}
		try {
			ObjectDatastreams datastreams = (ObjectDatastreams) this.getUnmarshaller().unmarshal(resp.getEntity().getContent());
			return datastreams;
		} catch (JAXBException e) {
			throw new IOException("Unabel to deserialize object profile", e);
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
	}

	public DatastreamProfile getDatastreamProfile(final String objectId, final String dsId) throws IOException {
		final HttpGet get = new HttpGet(fedoraUri.toASCIIString() + PATH_OBJECT_PROFILE + objectId + PATH_OBJECT_DATASTREAMS + dsId);
		final HttpResponse resp = client.execute(get);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Unable to fetch object profile from fedora: " + resp.getStatusLine().getReasonPhrase());
		}
		try {
			DatastreamProfile ds = (DatastreamProfile) this.getUnmarshaller().unmarshal(resp.getEntity().getContent());
			return ds;
		} catch (JAXBException e) {
			throw new IOException("Unabel to deserialize object profile", e);
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

	}
}