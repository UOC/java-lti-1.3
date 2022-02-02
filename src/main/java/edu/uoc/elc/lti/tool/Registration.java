package edu.uoc.elc.lti.tool;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Builder
public class Registration {
	private String id;
	private String clientId;
	private String name;
	private String platform;
	private String keySetUrl;
	private String accessTokenUrl;
	private String oidcAuthUrl;
	private KeySet keySet;
	private List<Deployment> deployments;

	public Deployment getDeployment(String deploymentId) {
		return Objects.nonNull(deployments) ? deployments.stream().filter(deployment -> deployment.getDeploymentId().equals(deploymentId)).findFirst().orElse(null) : null;
	}
}
