package br.com.efb.domain.security;

import br.com.efb.domain.entity.AuditEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Date;

public class AuditListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String remoteAddress = null;
        if (auth != null){
            final WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
            remoteAddress = details.getRemoteAddress();
        }

        AuditEntity revEntity = (AuditEntity) revisionEntity;

        if (auth != null){
            revEntity.setUsuario(auth.getName());
        } else {
            revEntity.setUsuario("System");
        }
        revEntity.setTimestamp(new Date().getTime());
        revEntity.setIp(remoteAddress);

    }
}
