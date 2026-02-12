import base.BaseTest;
import com.github.javafaker.Faker;
import config.BackendMicroservicesApiConfig;
import logging.ClientRestInterceptor;
import org.pet.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import service.PetApiService;
import testData.PetDataGenerator;
import utils.FakerService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {BackendMicroservicesApiConfig.class})
public class PetCreationTest extends BaseTest {

    @Autowired
    PetApiService petApiService;

    @Autowired
    PetDataGenerator petDataGenerator;

    private Faker faker = FakerService.get();

    @Test(groups = "smoke")
    public void addPetWithCorrectInfoAndUpdateTest() {
        Pet pet = petDataGenerator.generatePetWithCorrectInfo();
        petApiService.addPet(pet);
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(200);

        Pet createdPet = petApiService.getPetById(pet.getId());
        assertThat(createdPet).usingRecursiveComparison().isEqualTo(pet);

        Pet petForUpdate = petDataGenerator.generatePetWithCorrectInfo();
        petForUpdate.setId(pet.getId());
        petApiService.updatePet(petForUpdate);
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(200);

        Pet getUpdatedPet = petApiService.getPetById(pet.getId());
        assertThat(petForUpdate).usingRecursiveComparison().isEqualTo(getUpdatedPet);
    }

    @Test(groups = "smoke")
    public void addPetWithCorrectInfoAndFindItByStatus() {
        Pet pet = petDataGenerator.generatePetWithCorrectInfo();
        petApiService.addPet(pet);
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(200);

        List<Pet> petByStatus = petApiService.findPetByStatus(List.of(pet.getStatus().getValue()));
        Pet petFindByStatus = petByStatus
                .stream()
                .filter(x -> x.getId().equals(pet.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(petFindByStatus).usingRecursiveComparison().isEqualTo(pet);
    }

    @Test
    public void addPetWithCorrectInfoAndThenDelete() {
        Pet pet = petDataGenerator.generatePetWithCorrectInfo();
        petApiService.addPet(pet);
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(200);

        Pet createdPet = petApiService.getPetById(pet.getId());
        assertThat(createdPet).usingRecursiveComparison().isEqualTo(pet);

        petApiService.deletePetById(pet.getId());
        petApiService.tryToGetPetWithWrongId(pet.getId());
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(404);
        assertThat(ClientRestInterceptor.getResponseBody()).contains("Pet not found");
    }

    /**
     * Negative scenario
     *
     */
    // as far as we don't have any validation in the model for /pet POST,
    // we can only check that the pet is not found with wrong number
    @Test
    public void tryToGetPetWithWrongInfo() {
        petApiService.tryToGetPetWithWrongId(faker.number().randomNumber());
        assertThat(ClientRestInterceptor.getStatusCode()).isEqualTo(404);
        assertThat(ClientRestInterceptor.getResponseBody()).contains("Pet not found");
    }

}
