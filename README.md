# 🧩 Small Inventory System

## Overview

This project aims to simulate a small-scale **microservice-based inventory and order management system**.  
Designed primarily as a **learning project** to explore Spring Boot, RESTful communication between services, NoSQL databases and modular system design.

---

## 🏗️ Planned Architecture

The system will eventually consist of separate services, each running as an independent application:

| Service | Description | Status |
|---------|-------------|--------|
| **Inventory Service**| Manages shops, product categories, and product stock levels. Provides APIs for product CRUD operations and stock updates. | ✅ Bsic Implemented |
| **Order Service** | Manages shopping carts, reservations, and finalized orders. Tracks reserved stock and coordinates with the Inventory Service to confirm or roll back orders. | 🚧 In Progress |
| **Reward System** | Handles reward rule and calculation based on purchase quantity and total spending | 🔜 Planned |

---

## 🔁 Communication Between Services

- Services communicate using **HTTP REST APIs**.  
- Example: The Order Service may request product information or stock validation from the Inventory Service.  
- Future enhancements may include:
  - **Message queues** (RabbitMQ, Kafka) for event-driven updates  
  - **API Gateway** for unified routing  
  - **Service discovery** (Eureka, Consul)

---

## 📦 Current Component: Inventory Service

### Purpose
The Inventory Service acts as the backbone for managing all product-related data for a single shop.  
It provides APIs to create and manage:
- Shops  
- Product Categories  
- Products and their stock levels  

### Structure

