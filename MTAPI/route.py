from flask import jsonify, session, request

from app import app, bcrypt, db, User
from session import get_user, set_user, remove_user


@app.post("/user")
def route_user():

    user = get_user()
    if isinstance(user, tuple):
        return user

    return jsonify({
        "error": "success",
        "id": user.id,
        "username": user.name,
        "admin": user.admin,
        "created": user.created
    }), 200


@app.post("/user/password")
def route_user_password():

    user = get_user()
    if isinstance(user, tuple):
        return user

    password = request.json.get("password")

    if password is None:
        return jsonify({"error": "missing_fields", "message": "Missing fields! Require: [string] password"}), 200

    if not isinstance(password, str):
        return jsonify({"error": "invalid_field_types", "message": "Invalid field types! Require: [string] password"}), 200

    user.password = bcrypt.generate_password_hash(password)
    db.session.commit()

    return jsonify({
        "error": "success"
    }), 200


@app.post("/user/list")
def route_user_list():

    user = get_user()
    if isinstance(user, tuple):
        return user

    if not user.admin:
        return jsonify({"error": "missing_permission", "message": "Only administrators can list users!"}), 200

    users = []
    for user in User.query.all():
        users.append({
            "id": user.id,
            "name": user.name,
            "admin": user.admin,
            "created": user.created
        })

    return jsonify({
        "error": "success",
        "users": users
    }), 200


@app.post("/user/register")
def route_user_register():

    user = get_user()
    if isinstance(user, tuple):
        return user

    if not user.admin:
        return jsonify({"error": "missing_permission", "message": "Only administrators can register users!"}), 200

    username = request.json.get("username")
    password = request.json.get("password")
    admin = request.json.get("admin")

    if username is None or password is None or admin is None:
        return jsonify({"error": "missing_fields", "message": "Missing fields! Require: [string] username, [string] password, [boolean] admin"}), 200

    if not (isinstance(username, str) and isinstance(password, str) and isinstance(admin, bool)):
        return jsonify({"error": "invalid_field_types", "message": "Invalid field types! Require: [string] username, [string] password, [boolean] admin"}), 200

    if User.query.filter_by(name=username).first() is not None:
        return jsonify({"error": "user_exists", "message": "This username is already used!"}), 200

    hashed_password = bcrypt.generate_password_hash(password)
    user = User(name=username, password=hashed_password, admin=admin)
    db.session.add(user)
    db.session.commit()

    return jsonify({
        "error": "success",
        "id": user.id,
        "username": user.name,
        "admin": user.admin,
        "created": user.created
    }), 200


@app.post("/user/unregister")
def route_user_unregister():

    user = get_user()
    if isinstance(user, tuple):
        return user

    if not user.admin:
        return jsonify({"error": "missing_permission", "message": "Only administrators can unregister users!"}), 200

    username = request.json.get("username")

    if username is None:
        return jsonify({"error": "missing_fields", "message": "Missing fields! Require: [string] username"}), 200

    if not isinstance(username, str):
        return jsonify({"error": "invalid_field_types", "message": "Invalid field types! Require: [string] username"}), 200

    if username == "admin":
        return jsonify({"error": "delete_admin", "message": "The default admin user cannot be deleted!"}), 200

    if User.query.filter_by(name=username).first() is None:
        return jsonify({"error": "user_not_exists", "message": "This user doesn't exist!"}), 200

    User.query.filter_by(name=username).delete()
    db.session.commit()

    return jsonify({
        "error": "success"
    }), 200


@app.post("/user/login")
def route_user_login():

    username = request.json.get("username")
    password = request.json.get("password")

    if username is None or password is None:
        return jsonify({"error": "missing_fields", "message": "Missing fields! Require: [string] username, [string] password"}), 200

    if not (isinstance(username, str) and isinstance(password, str)):
        return jsonify({"error": "invalid_field_types", "message": "Invalid field types! Require: [string] username, [string] password"}), 200

    user = User.query.filter_by(name=username).first()

    if user is None:
        return jsonify({"error": "invalid_credentials", "message": "The username or password is incorrect!"}), 200

    if not bcrypt.check_password_hash(user.password, password):
        return jsonify({"error": "invalid_credentials", "message": "The username or password is incorrect!"}), 200

    set_user(user)

    return jsonify({
        "error": "success",
        "id": user.id,
        "username": user.name,
        "admin": user.admin,
        "created": user.created
    }), 200


@app.post("/user/logout")
def route_user_logout():

    user = get_user()
    if isinstance(user, tuple):
        return user

    remove_user()

    return jsonify({
        "error": "success"
    }), 200
