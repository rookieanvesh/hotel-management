# Hotel Booking System

A full-stack hotel booking application built with React, Spring Boot, and AWS, featuring secure user authentication and room management capabilities.

## Tech Stack

- **Frontend:** React
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Security:** Spring Security
- **Cloud:** AWS

## Features

- User authentication and authorization
- Room booking and management
- Admin dashboard
- Real-time room availability checking
- Secure payment processing
- Booking history tracking

## API Documentation

### Authentication Endpoints
```
POST /auth/register - Register a new user
POST /auth/login - User login
```

### Room Management Endpoints
```
GET /rooms/all - Get all rooms
GET /rooms/types - Get all room types
GET /rooms/room-by-id/{roomId} - Get room by ID
GET /rooms/all-available-rooms - Get all available rooms
GET /rooms/available-rooms-by-date-and-type - Get rooms by date and type
POST /rooms/add - Add new room (Admin only)
PUT /rooms/update/{roomId} - Update room details (Admin only)
DELETE /rooms/delete/{roomId} - Delete room (Admin only)
```

### Booking Endpoints
```
POST /bookings/book-room/{roomId}/{userId} - Book a room
GET /bookings/all - Get all bookings (Admin only)
GET /bookings/get-by-confirmation-code/{confirmationCode} - Get booking by confirmation code
DELETE /bookings/cancel/{bookingId} - Cancel booking (Admin only)
```

### User Management Endpoints
```
GET /users/all - Get all users (Admin only)
GET /users/get-by-id/{userId} - Get user by ID
DELETE /users/delete/{userId} - Delete user (Admin only)
GET /users/get-logged-in-profile-info - Get logged-in user profile
GET /users/get-user-bookings/{userId} - Get user booking history
```

## Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/hotel-booking-system.git
```

2. Backend Setup
```bash
cd backend
mvn clean install
```

3. Frontend Setup
```bash
cd frontend
npm install
```

4. Database Setup
- Create a PostgreSQL database
- Update `application.properties` with your database credentials

5. Configure AWS
- Set up AWS credentials
- Update AWS configuration in the application properties

## Running the Application

1. Start the backend server:
```bash
cd backend
mvn spring-boot:run
```

2. Start the frontend development server:
```bash
cd frontend
npm start
```

## Environment Variables

Create a `.env` file in the backend directory with the following variables:

```
DB_URL=your_database_url
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
JWT_SECRET=your_jwt_secret
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

