import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';
/*

This is a React functional component named `Navbar`. It renders a navigation bar with links to different pages, conditionally displaying certain links based on the user's authentication status and role.

Here's a breakdown:

* It uses the `ApiService` to check if the user is authenticated, an admin, or a regular user.
* It defines a `handleLogout` function to handle the logout process, which prompts the user to confirm and then navigates to the home page.
* The component returns a `nav` element with a brand link and an unordered list of links.
* The links are conditionally rendered based on the user's status:
	+ If the user is authenticated, they see a logout link.
	+ If the user is not authenticated, they see login and register links.
	+ If the user is an admin, they see an admin link.
	+ If the user is a regular user, they see a profile link.

Overall, this component provides a dynamic navigation bar that adapts to the user's role and authentication status. */
function Navbar() {
    const isAuthenticated = ApiService.isAuthenticated();
    const isAdmin = ApiService.isAdmin();
    const isUser = ApiService.isUser();
    const navigate = useNavigate();
/*

This code defines a `handleLogout` function that:

1. Prompts the user to confirm logout with a confirmation dialog.
2. If the user confirms, it calls the `ApiService.logout()` method to log out the user.
3. Then, it navigates the user to the `/home` page using the `navigate` function.

In essence, it handles the logout process with a confirmation step. */
    const handleLogout = () => {
        const isLogout = window.confirm('Are you sure you want to logout of this user?');
        if (isLogout) {
            ApiService.logout();
            navigate('/home');
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <NavLink to="/home">stayIn Hotel</NavLink>
            </div>
            <ul className="navbar-ul">
                <li><NavLink to="/home" activeclassname="active">Home</NavLink></li>
                <li><NavLink to="/rooms" activeclassname="active">Rooms</NavLink></li>
                <li><NavLink to="/find-booking" activeclassname="active">Find my Booking</NavLink></li>

                {isUser && <li><NavLink to="/profile" activeclassname="active">Profile</NavLink></li>}
                {isAdmin && <li><NavLink to="/admin" activeclassname="active">Admin</NavLink></li>}

                {!isAuthenticated &&<li><NavLink to="/login" activeclassname="active">Login</NavLink></li>}
                {!isAuthenticated &&<li><NavLink to="/register" activeclassname="active">Register</NavLink></li>}
                {isAuthenticated && <li onClick={handleLogout}>Logout</li>}
            </ul>
        </nav>
    );
}

export default Navbar;
