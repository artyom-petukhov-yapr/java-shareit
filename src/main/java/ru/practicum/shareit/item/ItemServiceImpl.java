package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto getItem(Integer id) {
        return itemMapper.mapToDto(itemRepository.get(id));
    }

    @Override
    public ItemDto createItem(ItemDto dto) {
        validateDto(dto);
        Item item = itemRepository.add(itemMapper.mapFromDto(dto));
        return itemMapper.mapToDto(item);
    }

    @Override
    public ItemDto patchItem(ItemDto dto) {
        Item item = itemRepository.patch(itemMapper.mapFromDto(dto));
        return itemMapper.mapToDto(item);
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> getUserItems(Integer userId) {
        List<Item> result = itemRepository.getUserItems(userId);
        return result.stream().map(item -> itemMapper.mapToDto(item)).toList();
    }

    @Override
    public List<ItemDto> findByText(String text) {
        List<Item> result = itemRepository.findByText(text);
        return result.stream().map(item -> itemMapper.mapToDto(item)).toList();
    }

    private void validateDto(ItemDto dto) {
        if (dto == null) {
            throw new ValidationException("Информация о предмете не может быть пустой");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException(String.format("Название предмета (id=%d) не может быть пустым", dto.getId()));
        }
        if (dto.getOwnerId() == null) {
            throw new ValidationException(String.format("Владелец предмета (id=%d) не может быть пустым", dto.getId()));
        }
    }
}
