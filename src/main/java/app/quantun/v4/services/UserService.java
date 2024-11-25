package app.quantun.v4.services;

import app.quantun.rop.v3.RailwayHandler;
import app.quantun.v4.contract.UserDTO;
import app.quantun.v4.model.User;

public class UserService {
    private UserRepository userRepository;
    public UserService() {
        this.userRepository = new UserRepository() {
            @Override
            public User save(User user) {
                User userWithId = new User(1L, user.name(), user.email()+"SAVE");
                return userWithId;
            }
        };
    }


    public RailwayHandler<UserDTO> createUser(UserDTO userDTO) {
        // Start the railway pipeline
        return RailwayHandler.success(userDTO)
                .flatMap(this::validateDTO)
                .flatMap(this::mapToEntity)
                .flatMap(this::saveEntity)
                .map(this::mapToDTO);
    }

    // Step 1: Validate DTO
    private RailwayHandler<UserDTO> validateDTO(UserDTO userDTO) {
        if (userDTO.name() == null || userDTO.name().isEmpty()) {
            return RailwayHandler.failure(new IllegalArgumentException("Name cannot be null or empty"));
        }
        if (userDTO.emai() == null || !userDTO.emai().contains("@")) {
            return RailwayHandler.failure(new IllegalArgumentException("Invalid email address"));
        }
        return RailwayHandler.success(userDTO);
    }

    // Step 2: Map DTO to Entity
    private RailwayHandler<User> mapToEntity(UserDTO userDTO) {
        try {
            User user = new User(null, userDTO.name(), userDTO.emai());
            return RailwayHandler.success(user);
        } catch (Exception e) {
            return RailwayHandler.failure(e);
        }
    }

    // Step 3: Save Entity to Database
    private RailwayHandler<User> saveEntity(User user) {
        try {
            User savedUser = userRepository.save(user);
            return RailwayHandler.success(savedUser);
        } catch (Exception e) {
            return RailwayHandler.failure(e);
        }
    }

    // Step 4: Map Entity back to DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.name(), user.email());
    }
}
