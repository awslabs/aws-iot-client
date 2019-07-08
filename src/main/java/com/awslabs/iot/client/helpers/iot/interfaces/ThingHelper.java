package com.awslabs.iot.client.helpers.iot.interfaces;

import com.amazonaws.services.iot.model.ThingAttribute;
import com.awslabs.iot.client.helpers.iot.exceptions.ThingAttachedToPrincipalsException;

import java.util.List;
import java.util.Optional;

public interface ThingHelper {
    List<String> listThingNames();

    List<ThingAttribute> listThingAttributes();

    void delete(String name) throws ThingAttachedToPrincipalsException;

    List<String> listPrincipals(String thingName);

    List<String> listPrincipalThings(String principal);

    void detachPrincipal(String thingName, String principal);

    List<String> detachPrincipals(String name);

    void deletePrincipal(String principal);

    boolean principalAttachedToImmutableThing(String principal);

    boolean isThingImmutable(String thingName);

    boolean isThingArnImmutable(String thingArn);

    Optional<ThingAttribute> getThingIfItExists(String thingArn);
}
