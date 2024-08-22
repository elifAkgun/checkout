package code.elif.checkout.entity.items;

import code.elif.checkout.enums.ItemType;
import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.exception.ItemFactoryException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemFactoryTest {

    @Test
    void givenDefaultItemType_whenCreateItem_thenItemShouldBeDefaultItem() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();

        // When
        Item item = ItemFactory.createItem(itemDto);

        // Then
        assertTrue(item instanceof DefaultItem);
    }

    @Test
    void givenDigitalItemType_whenCreateItem_thenItemShouldBeDigitalItem() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(7889)
                .sellerId(123)
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .itemType(ItemType.DIGITAL)
                .build();

        // When
        Item item = ItemFactory.createItem(itemDto);

        // Then
        assertTrue(item instanceof DigitalItem);
    }

    @Test
    void givenVasItemType_whenCreateItem_thenItemShouldBeVasItem() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(3242)
                .sellerId(5003)
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .itemType(ItemType.VAS)
                .build();

        // When
        Item item = ItemFactory.createItem(itemDto);

        // Then
        assertTrue(item instanceof VasItem);
    }

    @Test
    void givenUnsupportedItemType_whenCreateItem_thenShouldThrowException() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .itemType(null) // Unsupported item type
                .build();

        // When / Then
        Exception exception = assertThrows(ItemFactoryException.class, () -> ItemFactory.createItem(itemDto));
        String expectedMessage = "Unsupported item type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
