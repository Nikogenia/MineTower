from flask import session, jsonify

from sql import User


def get_user():

    user_id = session.get("user_id")

    if not user_id:
        return jsonify({"error": "unauthorized", "message": "You need to log in!"}), 200

    user = User.query.filter_by(id=user_id).first()
    if user is None:
        remove_user()
        return jsonify({"error": "invalid_session", "message": "Your session is invalid! Please log in again!"}), 200

    return user


def set_user(user):

    session["user_id"] = user.id


def remove_user():

    session.pop("user_id")
