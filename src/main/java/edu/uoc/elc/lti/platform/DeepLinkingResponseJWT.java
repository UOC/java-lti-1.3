package edu.uoc.elc.lti.platform;

import edu.uoc.elc.lti.exception.LTISignatureException;
import edu.uoc.elc.lti.tool.ClaimsEnum;
import edu.uoc.elc.lti.tool.MessageTypesEnum;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.lti.tool.deeplinking.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class DeepLinkingResponseJWT {
	private final static long _5_MINUTES = 5 * 30 * 1000;

	private final String platformName;
	private final String toolName;
	private final String azp;

	private final String publicKey;
	private final String privateKey;
	private final String deploymentId;
	private final Settings settings;
	private final List<Item> itemList;

	@Setter
	private String message;
	@Setter
	private String log;
	@Setter
	private String errorMessage;
	@Setter
	private String errorLog;

	private SecureRandom secureRandom = new SecureRandom();

	/*
	String build() {
		try {
			AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);
			Algorithm algorithm = algorithmFactory.getAlgorithm();
			byte bytes[] = new byte[10];
			secureRandom.nextBytes(bytes);

			final JWTCreator.Builder builder = JWT.create()
							.withIssuer(toolName)
							.withAudience(platformName)
							.withClaim("azp", this.azp)
							.withIssuedAt(new Date())
							.withExpiresAt(new Date(System.currentTimeMillis() + _5_MINUTES))
							.withJWTId(new String(bytes))
							.withClaim(ClaimsEnum.MESSAGE_TYPE.getName(), MessageTypesEnum.LtiDeepLinkingRequest.name())
							.withClaim(ClaimsEnum.VERSION.getName(), "1.3.0")
							.withClaim(ClaimsEnum.DEPLOYMENT_ID.getName(), deploymentId);

			if (this.settings.getData() != null) {
				builder.withClaim(ClaimsEnum.DEEP_LINKING_DATA.getName(), this.settings.getData());
			}

			if (this.message != null) {
				builder.withClaim(ClaimsEnum.DEEP_LINKING_MESSAGE.getName(), this.message);
			}

			if (this.log != null) {
				builder.withClaim(ClaimsEnum.DEEP_LINKING_LOG.getName(), this.log);
			}

			if (this.errorMessage != null) {
				builder.withClaim(ClaimsEnum.DEEP_LINKING_ERROR_MESSAGE.getName(), this.errorMessage);
			}

			if (this.errorLog != null) {
				builder.withClaim(ClaimsEnum.DEEP_LINKING_ERROR_LOG.getName(), this.errorLog);
			}

			// TODO: content items


			return builder.sign(algorithm);
		} catch (JWTCreationException exception){
			//Invalid Signing configuration / Couldn't convert Claims.
			throw new LTISignatureException(exception);
		}
	}
	 */
}
