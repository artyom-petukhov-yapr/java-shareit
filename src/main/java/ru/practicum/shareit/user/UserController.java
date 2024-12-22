package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Создать пользователя
     */
    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    /**
     * Обновить данные пользователя
     */
    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Integer id, @RequestBody UserDto user) {
        user.setId(id);
        return userService.patchUser(user);
    }

    /**
     * Получить данные пользователя
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    /**
     * Удалить пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
