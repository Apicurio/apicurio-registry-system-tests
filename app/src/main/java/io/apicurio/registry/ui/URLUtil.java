package io.apicurio.registry.ui;

import io.apicurio.common.apps.config.Info;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Utility to generate absolute URLs.
 *
 * @author Jakub Senko <em>m@jsenko.net</em>
 */
@ApplicationScoped
public class URLUtil {

    @ConfigProperty(name = "registry.url.override.host")
    @Info(category = "redirects", description = "Override the hostname used for generating externally-accessible URLs. " +
            "The host and port overrides are useful e.g. when deploying with HTTPS passthrough ingress/route on OpenShift. " +
            "In case like these, the request URL/IP (and port) that is then re-used for redirection " +
            "does not belong to actual external URL used by the client, because the request is proxied. " +
            "The redirection than fails because the target URL is not reachable.", availableSince = "2.5.0.Final")
    Optional<String> urlOverrideHost;

    @ConfigProperty(name = "registry.url.override.port")
    @Info(category = "redirects", description = "Override the port used for generating externally-accessible URLs.", availableSince = "2.5.0.Final")
    Optional<Integer> urlOverridePort;

    @Inject
    Logger log;

    /**
     * Given a relative path to a resource on this Registry server,
     * try to produce an externally-accessible absolute URL to it, based on the request or configuration.
     * This is useful for redirects and generating URLs for clients.
     */
    public URL getExternalAbsoluteURL(HttpServletRequest request, String relativePath) {

        String targetProtocol = null;
        String targetHost = null;
        int targetPort = -1;

        try {

            String requestURLStr = request.getRequestURL().toString();
            URL requestURL = new URL(requestURLStr);

            var forwardedProtoHeaderValue = request.getHeader("X-Forwarded-Proto");
            var forwardedHostHeaderValue = request.getHeader("X-Forwarded-Host");

            // Protocol
            targetProtocol = requestURL.getProtocol();
            if ("http".equals(targetProtocol) && request.isSecure()) {
                log.debug("Generating absolute URL: Switching from HTTP to HTTPS protocol for a secure request.");
                targetProtocol = "https";
            }
            if (forwardedProtoHeaderValue != null && !forwardedProtoHeaderValue.isBlank()) {
                log.debug("Generating absolute URL: Using X-Forwarded-Proto header value for the protocol.");
                targetProtocol = forwardedProtoHeaderValue;
            }

            // Host
            targetHost = requestURL.getHost();
            if (urlOverrideHost.isPresent() && !urlOverrideHost.get().isBlank()) {
                log.debug("Generating absolute URL: Using configured override for the host.");
                targetHost = urlOverrideHost.get();
            } else if (forwardedHostHeaderValue != null && !forwardedHostHeaderValue.isBlank()) {
                log.debug("Generating absolute URL: Using X-Forwarded-Host header value for the host.");
                targetHost = forwardedHostHeaderValue;
            }

            // Port
            targetPort = requestURL.getPort();
            if (urlOverridePort.isPresent() && urlOverridePort.get() > 0) {
                log.debug("Generating absolute URL: Using configured override for the port.");
                targetPort = urlOverridePort.get();
            }

            if (("https".equals(targetProtocol) && targetPort == 443)
                    || ("http".equals(targetProtocol) && targetPort == 80)) {
                targetPort = -1;
            }

            var targetURL = new URL(targetProtocol, targetHost, targetPort, relativePath);
            log.debug("Generating absolute URL: {} -> {}", requestURL, targetURL);

            return targetURL;

        } catch (MalformedURLException ex) {
            throw new RuntimeException(String.format("Could not generate a valid absolute URL from: " +
                            "protocol = '%s', host = '%s', port = '%s', and relativePath = '%s'.",
                    targetProtocol, targetHost, targetPort, relativePath), ex);
        }
    }
}