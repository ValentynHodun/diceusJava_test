package service;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pet.api.PetApi;
import org.pet.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
@AllArgsConstructor
public class PetApiService {

    @Autowired
    PetApi petApi;

    @Step
    public void addPet(Pet body) {
        petApi.addPetWithHttpInfo(body);
    }

    @Step
    public void tryToAddPetWithWrongInfo(Pet body) {
        try {
            addPet(body);
        } catch (Exception e) {
            log.info("add pet error: " + e.getMessage());
        }
    }

    @Step
    public Pet getPetById(Long petId) {
        return petApi.getPetById(petId);
    }

    @Step
    public void tryToGetPetWithWrongId(Long id) {
        try {
            getPetById(id);
        } catch (Exception e) {
            log.info("get pet error: " + e.getMessage());
        }
    }

    @Step
    public void updatePet(Pet body) {
        petApi.updatePet(body);
    }

    @Step
    public List<Pet> findPetByStatus(List<String> status) {
        return petApi.findPetsByStatus(status);
    }

    @Step
    public void deletePetById(Long petId) {
        petApi.deletePet(petId, null);
    }
}
