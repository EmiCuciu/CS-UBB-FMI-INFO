package org.example.grile.Repository;

import org.example.grile.Domain.MenuItem;
import org.example.grile.Domain.Validation.MenuItemValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {
    private final String url;
    private final String user;
    private final String password;
    private final MenuItemValidator menuItemValidator;

    public MenuItemRepository(String url, String user, String password, MenuItemValidator menuItemValidator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.menuItemValidator = menuItemValidator;
    }


    public List<MenuItem> findAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                menuItems.add(new MenuItem(
                        resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getString("item"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return menuItems;
    }

    public MenuItem findById(Integer id) {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new MenuItem(
                        resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getString("item"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void save(MenuItem menuItem) {
        menuItemValidator.validate(menuItem);
        String sql = "INSERT INTO menu (category, item, price, currency) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, menuItem.getCategory());
            preparedStatement.setString(2, menuItem.getItem());
            preparedStatement.setFloat(3, menuItem.getPrice());
            preparedStatement.setString(4, menuItem.getCurrency());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
