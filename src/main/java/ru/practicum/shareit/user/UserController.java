package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String ID_PATH = "/{id}";

    /**
     * Создать пользователя
     */
    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        User result = userService.createUser(modelMapper.map(user, User.class));
        return modelMapper.map(result, UserDto.class);
    }

    /**
     * Обновить данные пользователя
     */
    @PatchMapping(ID_PATH)
    public UserDto patchUser(@PathVariable Integer id, @RequestBody UserDto user) {
        user.setId(id);
        User result = userService.patchUser(modelMapper.map(user, User.class));
        return modelMapper.map(result, UserDto.class);
    }

    /**
     * Получить данные пользователя
     */
    @GetMapping(ID_PATH)
    public UserDto getUser(@PathVariable Integer id) {
        return modelMapper.map(userService.getUser(id), UserDto.class);
    }

    /**
     * Удалить пользователя
     */
    @DeleteMapping(ID_PATH)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
