

# BackendAPI
Este proyecto es una práctica de desarrollo de microservicios que consume datos de la PokeAPI.
Está construido utilizando Java y Maven, y tiene como objetivo mostrar mis habilidades consumiendo apis y utilizando sus datos
para crear informacion y nuevas funciones

Características :
Implementación de microservicios en Java.

Consumo de la PokeAPI para obtener información de Pokémon.

Estructura del proyecto basada en Maven.

Diseño enfocado en la escalabilidad y mantenibilidad.

Tecnologias:
-
+ Java 21
+ Maven
+ MySQL
+ Mongo
+ Redis
+ Lombock
+ JWT
+ Spring Security
+ Docker
+ WebClient

## 📌 ¿Qué puedes hacer con esta app?

- 🔐 Registrar e iniciar sesión como usuario.
- 🔎 Consultar Pokémon por generación o tipo.
- 🧩 Resolver acertijos de nombres mezclados.
- 🎒 Gestionar una mochila con dinero e ítems.
- 🛒 Comprar ítems evolutivos o consumibles.
- 🧬 Capturar Pokémon y asignarles ítems.
- 🧑‍🤝‍🧑 Crear equipos y agregar o intercambiar Pokémon.
- ⭐ Crear y gestionar una lista de deseos (wishlist).
- 🏡 Dejar Pokémon en la guardería (daycare).
- 🔁 Intercambiar Pokémon con otros entrenadores.

# 📦 BackendAPI Pokémon App

Esta es la documentación del backend para la aplicación de gestión de Pokémon. La API permite registrar usuarios, iniciar sesión, consultar información de Pokémon, gestionar mochilas, resolver acertijos, comprar ítems, formar equipos, capturar Pokémon y mucho más.

## 📌 ¿Qué puedes hacer con esta app?

- 🔐 Registrar e iniciar sesión como usuario.
- 🔎 Consultar Pokémon por generación o tipo.
- 🧩 Resolver acertijos de nombres mezclados.
- 🎒 Gestionar una mochila con dinero e ítems.
- 🛒 Comprar ítems evolutivos o consumibles.
- 🧬 Capturar Pokémon y asignarles ítems.
- 🧑‍🤝‍🧑 Crear equipos y agregar o intercambiar Pokémon.
- ⭐ Crear y gestionar una lista de deseos (wishlist).
- 🏡 Dejar Pokémon en la guardería (daycare).
- 🔁 Intercambiar Pokémon con otros entrenadores.


## Documentation

[Documentation](https://linktodocumentation)


## 📂 Endpoints Públicos

Estos endpoints no requieren autenticación.

🔐 /auth

🔑 /login
Tipo: POST  
  {
    "username": "string",
    "password": "string"
  }
  Descripción: Inicia sesión con credenciales válidas y retorna un token JWT para acceder a los endpoints privados.

📝 /register
Tipo: POST

Body:
{
  "username": "string",
  "password": "string",
  "email": "string"
}

Descripción: Registra un nuevo usuario. Todos los campos son obligatorios. El correo debe ser válido. El nombre de usuario debe tener al menos 3 caracteres y la contraseña, al menos 6.

🐾 /pokemon

+ /
Tipo: GET

Params: generation

Descripción: Retorna una lista con los nombres de los Pokémon de una generación específica. Ejemplo: ?generation=1 o ?generation=generation-I.

### /types

Tipo: GET

Params: type

Descripción: Retorna Pokémon que tienen ese tipo como primario o secundario.

### /generation

Tipo: GET

Params: generation

Descripción: Retorna un mapa con IDs y nombres de los Pokémon de esa generación.

### /evolution
Tipo: GET

Params: String id

Descripción: Informa el trigger de evolución del Pokémon (nivel, ítem, etc.) o indica si no tiene evolución.

### /random
Tipo: GET

Descripción: Retorna el nombre de un Pokémon aleatorio entre las 9 generaciones.


#  🔒 Endpoints Privados
Requieren token JWT. Se obtiene en /auth/login.

## 🎒 /bag
### /
Tipo: GET

Descripción: Retorna un JSON con:

+ money
+ objetosEvolutivos
+ objetosConsumibles

### /money/check
Tipo: GET

Descripción: Consulta la cantidad actual de dinero.

## ❓ /questions
### /
Tipo: GET

Descripción: Retorna el nombre de un Pokémon con letras desordenadas. El acertijo se almacena en Redis.

### /answer
Tipo: POST

Params: String answer

Descripción: Valida la respuesta. Si es correcta, otorga 100 monedas y elimina el acertijo de Redis.

### /answer/get
Tipo: GET

Descripción: Muestra la respuesta correcta al acertijo y la elimina de Redis.

## 🛍️ /shop
### /
Tipo: GET

Descripción: Muestra los ítems disponibles en la tienda con su precio.

### /buy

Tipo: POST

Params: String type, String item

Descripción: Compra un ítem si el usuario tiene suficiente dinero. Tipos válidos: Consumable, Evolution.

## 👥 /team
### /
Tipo: GET

Descripción: Retorna un DTO con el nombre del equipo, miembros y su información (exp, vida, id, entrenador original).

### /
Tipo: POST

Params: String name

Descripción: Crea un nuevo equipo. Si ya existe uno, lo informa.

### /add
Tipo: POST

Params: long idPokemon

Descripción: Agrega un Pokémon al equipo si está en estado PC.

### /change
Tipo: POST

Params: long idPokemon, long idPokemonTeam

Descripción: Intercambia un Pokémon del equipo por uno en estado PC.

## 🎯 /wishlist
### /
Tipo: GET

Descripción: Muestra los ítems en la wishlist del usuario.

### /create
Tipo: POST

Descripción: Crea una nueva wishlist. Si ya existe, lo indica.

### /verify
Tipo: GET

Params: String item

Descripción: Verifica si el ítem está en la wishlist.

### /add
Tipo: POST

Params: String item

Descripción: Agrega un ítem a la wishlist. No permite duplicados ni ítems inválidos.

### /delete

Tipo: DELETE

Params: String item

Descripción: Elimina el ítem de la wishlist.

## 👤 /user 

## ⚠️ Notas importantes
***El idPokemon solicitado corresponde al ID de asociación entre el Pokémon y el usuario, no al ID original del Pokémon.
Este ID se genera al capturar un Pokémon o puede consultarse en los detalles del equipo.***


![image](https://github.com/user-attachments/assets/8ba8588d-111f-4cec-9775-ee651650d7ff)

![image](https://github.com/user-attachments/assets/0dce6e39-9c31-41d1-ad9c-554053ea0192)

![image](https://github.com/user-attachments/assets/f1ee5645-3ac7-414e-ba99-1a4916770040)


### /

Tipo: GET

Descripción: Muestra todos los Pokémon del usuario.

### /status
Tipo: GET

Params: String status

Descripción: Muestra los Pokémon con ese estado (PC, Team, DC, Swap).

### /pokemon
Tipo: GET

Params: int id

Descripción: Retorna los detalles del Pokémon con ID de asociación al usuario.

### /caught
Tipo: POST

Params: string pokemon

Descripción: Intenta capturar el Pokémon. Si tiene éxito, se asocia al usuario.

### /item/add
Tipo: POST

Params: String item, int pokemonId

Descripción: Usa un ítem en un Pokémon. Si es medicinal, recupera vida. Si es equipable, reemplaza el anterior.

### /item/remove

Tipo: PATCH

Params:int pokemonId

Descripción: Quita el ítem del Pokémon, si tiene uno.

### /pokemon/change/name
Tipo: PATCH

Params: int pokemonId, String name

Descripción: Cambia el nombre del Pokémon.

### 🔁 Intercambio
### /swap

Tipo: POST

Params: long idPokemon

Descripción: Crea una sala de intercambio. El Pokémon cambia su estado a Swap.

### /swap/other
Tipo: POST

Params: long idPokemon, string trainer

Descripción: Se intercambia un Pokémon con otro entrenador. Ambos terminan en estado PC.

### 🏡 Guardería
### /daycare
Tipo: POST

Params: long idPokemon

Descripción: Envía un Pokémon a la guardería. Si ya hay uno, lo informa.

### /daycare/collect
Tipo: GET

Params: long idPokemon

Descripción: Recupera al Pokémon, calcula el tiempo y experiencia ganada. Si cumple condiciones de evolución, evoluciona.
