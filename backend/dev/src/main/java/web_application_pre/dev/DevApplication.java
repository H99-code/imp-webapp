package web_application_pre.dev;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import web_application_pre.dev.entity.Item;
import web_application_pre.dev.entity.User;
import web_application_pre.dev.repository.ItemRepository;
import web_application_pre.dev.repository.UserRepository;

@SpringBootApplication
public class DevApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	public static void main(String[] args) {
		SpringApplication.run(DevApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		User user = new User();
		user.setId("f9b1f666-ef55-423d-b2be-4ff74aed4656");
		user.setUsername("TestUser");
		user.setEmail("testuser@example.com");


		userRepository.save(user);
		System.out.println("Before saving user: " + user);
		System.out.println("After saving user: " + user);


		Item item1 = new Item();
		item1.setName("TestItem 1");
		item1.setLink("http://example.com/item1");
		item1.setType("TestType");
		item1.setPrice("10");

		item1.setUser(user);

		Item item2 = new Item();
		item2.setName("TestItem 2");
		item2.setLink("http://example.com/item2");
		item2.setType("TestType");
		item2.setPrice("20");
		item2.setUser(user);


		System.out.println("Before saving item: " + item1);
		itemRepository.save(item1);
		itemRepository.save(item2);


		System.out.println("After saving item: " + item1);

		User updatedUser = userRepository.findByIdWithItems(user.getId()).orElse(null);

		if (updatedUser != null) {
			System.out.println("Items für User " + updatedUser.getUsername() + ":");
			updatedUser.getItems().forEach(i -> {
				System.out.println("Name: " + i.getName());
				System.out.println("Link: " + i.getLink());
				System.out.println("Type: " + i.getType());
				System.out.println("Price: " + i.getPrice());
				System.out.println("-------------------------");
			});
		}
	}
}
