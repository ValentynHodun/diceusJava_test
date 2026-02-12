package testData;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pet.model.Category;
import org.pet.model.Pet;
import org.pet.model.Tag;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import utils.FakerService;

import java.util.List;

import static org.pet.model.Pet.StatusEnum.AVAILABLE;

@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PetDataGenerator {

    private Faker faker = FakerService.get();

    public Pet generatePetWithCorrectInfo() {
        return new Pet() {{
            setName(faker.name().username());
            setCategory(new Category() {{
                setId(1L);
                setName("Category1");
            }});
            setId(faker.number().randomNumber());
            setTags(List.of(new Tag() {{
                setId(1L);
                setName("Tag1");
            }}, new Tag() {{
                setId(2L);
                setName("Tag2");
            }}));
            setStatus(AVAILABLE);
            setPhotoUrls(List.of(faker.internet().url()));
        }};
    }

}
