package code.elif.checkout.entity.cart;

import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.entity.items.ItemFactory;
import code.elif.checkout.entity.items.VasItem;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.exception.CartException;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    void givenVasItemAndNoParentItem_whenAddItem_thenThrowsCartException() {
        // Given
        ItemDto vasItemDto = new ItemDto(1, 3242, 5003, BigDecimal.TEN, 1, null, ItemType.VAS);

        //  When & Assert
        Item newItem = ItemFactory.createItem(vasItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("VasItems can only be added with parent item.", exception.getMessage());
    }

    @Test
    void givenVasItemWithNonFurnitureOrElectronicsParentItem_whenAddItem_thenThrowsCartException() {
        // Given
        ItemDto parentItemDto = new ItemDto(1, 2000, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT);
        cart.addItem(ItemFactory.createItem(parentItemDto));

        ItemDto vasItemDto = new ItemDto(2, 3242, 5003, BigDecimal.TEN, 1, 1, ItemType.VAS);

        //  When & Assert
        Item newItem = ItemFactory.createItem(vasItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("VasItem can only be added to a DefaultItem in the Furniture (1001) or Electronics (3004) categories.", exception.getMessage());
    }

    @Test
    void givenParentItemWithMaxVasItems_whenAddItem_thenThrowsCartException() {
        // Given
        DefaultItem parentItem = new DefaultItem(1, 1001, 1, new Price(BigDecimal.TEN), new Quantity(1));
        for (int i = 0; i < 3; i++) {
            VasItem vasItem = new VasItem(i + 2, 1, 3242, 5003, new Price(BigDecimal.TEN), new Quantity(1));
            parentItem.addVasItem(vasItem);
        }
        cart.addItem(parentItem);

        ItemDto vasItemDto = new ItemDto(5, 3242, 5003, BigDecimal.TEN, 1, 1, ItemType.VAS);

        //  When & Assert
        Item newItem = ItemFactory.createItem(vasItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("A maximum of 3 VasItems can be added to a DefaultItem.", exception.getMessage());
    }

    @Test
    void givenVasItemWithValidParentItem_whenAddItem_thenAddsToParentItem() {
        // Given
        DefaultItem parentItem = new DefaultItem(1, 1001, 1, new Price(BigDecimal.TEN), new Quantity(1));
        cart.addItem(parentItem);

        ItemDto vasItemDto = new ItemDto(2, 3242, 5003, BigDecimal.TEN, 1, 1, ItemType.VAS);

        //  When
        cart.addItem(ItemFactory.createItem(vasItemDto));

        //  Then
        assertEquals(1, parentItem.getVasItems().size());
    }


    @Test
    void givenCartWithMaxUniqueItems_whenAddItem_thenThrowsCartException() {
        // Given
        for (int i = 0; i < 10; i++) {
            ItemDto itemDto = new ItemDto(1, 1, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT);
            cart.addItem(ItemFactory.createItem(itemDto));
        }

        //  When & Assert
        ItemDto newItemDto = new ItemDto(1, 1, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT);
        Item newItem = ItemFactory.createItem(newItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("The maximum quantity of an item that can be added is 10.", exception.getMessage());
    }

    @Test
    void givenCartWithMaxTotalProducts_whenAddItem_thenThrowsCartException() {
        // Given
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = 0; i < 30; i++) {
            ItemDto itemDto = new ItemDto(i % 3, 1, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT);
            cart.addItem(ItemFactory.createItem(itemDto));
            totalPrice = totalPrice.add(BigDecimal.TEN);
        }

        //  When & Assert
        ItemDto newItemDto = new ItemDto(4, 1, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT);
        Item newItem = ItemFactory.createItem(newItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("The total number of products cannot exceed 30.", exception.getMessage());
    }

    @Test
    void givenCartWithMaxTotalAmount_whenAddItem_thenThrowsCartException() {
        // Given
        for (int i = 0; i < 5; i++) {
            ItemDto itemDto = new ItemDto(i + 1, 1001, 1, new BigDecimal(90000), 1, null,ItemType.DEFAULT);
            cart.addItem(ItemFactory.createItem(itemDto));
        }
        // Add VasItem with price 10 TL
        ItemDto vasItemDto = new ItemDto(6, 3242, 5003, new BigDecimal(50000), 1, 1, ItemType.VAS);
        cart.addItem(ItemFactory.createItem(vasItemDto));

        //  When & Assert
        ItemDto newItemDto = new ItemDto(7, 1, 1, BigDecimal.TEN, 1, null, ItemType.DEFAULT); // Adding an item with price 10 TL
        Item newItem = ItemFactory.createItem(newItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("The total amount of the cart cannot exceed 500,000 TL.", exception.getMessage());
    }

    @Test
    void givenDigitalItemWithMaxQuantity_whenAddItem_thenThrowsCartException() {
        // Given
        ItemDto digitalItemDto = new ItemDto(1, 7889, 1, BigDecimal.TEN, 5, null, ItemType.DIGITAL);
        cart.addItem(ItemFactory.createItem(digitalItemDto));

        //  When & Assert
        ItemDto newDigitalItemDto = new ItemDto(2, 7889, 1, BigDecimal.TEN, 1, null, ItemType.DIGITAL);
        Item newDigitalItem = ItemFactory.createItem(newDigitalItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newDigitalItem));
        assertEquals("The maximum quantity of a DigitalItem that can be added is 5.", exception.getMessage());
    }

    @Test
    void givenDefaultItemWithHigherPricedVasItem_whenAddItem_thenThrowsCartException() {
        // Given
        DefaultItem parentItem = new DefaultItem(1, 1001, 1, new Price(new BigDecimal(100)), new Quantity(1));
        cart.addItem(parentItem);

        ItemDto vasItemDto = new ItemDto(2, 3242, 5003, new BigDecimal(200), 1, 1, ItemType.VAS);

        //  When & Assert
        Item newItem = ItemFactory.createItem(vasItemDto);
        CartException exception = assertThrows(CartException.class, () -> cart.addItem(newItem));
        assertEquals("The price of the VasItem added to the DefaultItem cannot be higher than the DefaultItem's price.", exception.getMessage());
    }

}
