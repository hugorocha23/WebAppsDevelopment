//package pt.isel.daw.g4.app;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import pt.isel.daw.g4.app.database.entity.ChecklistTemplateEntity;
//import pt.isel.daw.g4.app.database.repository.ChecklistTemplateRepository;
//import pt.isel.daw.g4.app.database.service.ChecklistTemplateService;
//import pt.isel.daw.g4.app.model.input_model.ChecklistTemplateInputModel;
//import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateOutputModel;
//
//import java.util.Optional;
//
//import static org.springframework.util.Assert.isTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ChecklistTemplateServiceTests {
//
//	@Autowired
//	private ChecklistTemplateService service;
//	@Autowired
//	private ChecklistTemplateRepository repo;
//
//	private static ChecklistTemplateInputModel input = new ChecklistTemplateInputModel();
//	static {
//		input.name = "Testing";
//		input.description = "Hello there!!!";
//	}
//
//
//	@Test
//	public void CreateChecklistTemplate() {
//		Long id = service.create(input, userID);
//		Optional<ChecklistTemplateEntity> hasEntity = repo.findById(id);
//		isTrue(hasEntity.isPresent());
//		ChecklistTemplateEntity entity = hasEntity.get();
//		isTrue(entity.name.equals(input.name));
//		isTrue(entity.description.equals(input.description));
//	}
//
//	@Test
//	public void ReadChecklistTemplate() throws NoSuchFieldException, IllegalAccessException {
//		ChecklistTemplateEntity entity = new ChecklistTemplateEntity();
//		entity.name = "Hello";
//		entity.description = "World";
//		entity = repo.save(entity);
//
//		ChecklistTemplateOutputModel model = service.readById(entity.pk, userID);
//
//		Class propertiesClass = model.properties.getClass();
//
//		isTrue(propertiesClass.getField("name").get(model.properties).equals(entity.name));
//		isTrue(propertiesClass.getField("description").get(model.properties).equals(entity.description));
//
//	}
//
//	@Test
//	public void CreateChecklistFromTemplate(){
//
//	}
//
//
//}
