# junit5-randomizer-extension

> The randomizer extension for JUnit5 is just a convenient wrapper around [easy-random](https://github.com/j-easy/easy-random), a sort of dumb [ObjectMother](https://martinfowler.com/bliki/ObjectMother.html) for the JVM.

## Basic usage

### Single entities

```java
@ExtendWith(RandomizerExtension.class)
class CreateOrderUseCaseImplTest {
  
  @Random
  private MyObject randomField;
  
  //...

  @Test
  void shouldNotCreateOrder_WhenOrderTypeIsMissing(@Random Order newOrder) {
		// given
    newOrder.setType(null);

    // when
    createOrderUseCase.execute(newOrder);
    
    // then
    verify(orderRepository, never()).save(any());
  }
}
```

### Collections

```java
@Test
void givenItemListTooLong_thenReturnBadRequest(
  @Random(type=ItemDto.class, size=30) List<ItemDto> manyItems) {
  //...
}
```

## Advanced usage

### Custom mappings

```java
@ExtendWith(RandomizerExtension.class)
class EntityApiControllerUpdateItemsTest implements CustomRandomFieldProvider {

  @Override
  public void registerCustomRandomizers(CustomRandomizerRegistry registry) {
    registry.registerRandomizer(
      StringField.named("state"),
      StringRandomizerFactory.ofEnum(SubmissionState.class));
  }
}
```


