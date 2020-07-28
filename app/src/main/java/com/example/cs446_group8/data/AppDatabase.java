package com.example.cs446_group8.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Crop.class}, exportSchema = true, version = 1)
@TypeConverters(CropType.Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CropDao cropDao();

    /**
     * A weak map is used to keep track of the app database instances so that {@link #createPrepopulateCallback}
     * can have access.
     */
    private static final WeakHashMap<Context, AppDatabase> DATABASE_INSTANCES = new WeakHashMap<>();

    /**
     * Creates or opens a pre-populated app database.
     *
     * @param context the current application context
     * @return opened database
     */
    public synchronized static AppDatabase getInstance(Context context) {
        AppDatabase instance = DATABASE_INSTANCES.get(context);
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "app_database.db")
                    .createFromAsset("app_database.db")
                    .build();
            DATABASE_INSTANCES.put(context, instance);
        }
        return instance;
    }

    /**
     * The execution context in which the prepopulation will be performed. If you rely on the
     * prepopulated data to exist, schedule a {@link Runnable} on here.
     */
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * The room callback used to prepopulate the app database.
     */
    static Callback createPrepopulateCallback(Context context) {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                IO_EXECUTOR.execute(() -> prepopulateDatabase(getInstance(context)));
            }
        };
    }

    /**
     * Static crop data.
     */
    static final List<Crop> CROPS = Collections.unmodifiableList(Arrays.asList(
            new Crop(1, "Amaranth leaves", 371, CropType.Green, 110, 1, 40, 53),
            new Crop(2, "Arrow roots", 100, CropType.Colorful, 60, 5, 27, 38),
            new Crop(3, "Artichoke", 47, CropType.Colorful, 180, 1.2, 50, 2),
            new Crop(4, "Beets", 43, CropType.Starch, 50, 0.5, 25, 37),
            new Crop(5, "Black nightshade", 42, CropType.Green, 60, 1, 20, 31),
            new Crop(6, "Broccoli", 34, CropType.Starch, 80, 6.4, 40, 15),
            new Crop(7, "Brussels sprouts", 43, CropType.Starch, 100, 0.4, 60, 12),
            new Crop(8, "Cabbage", 22, CropType.Green, 70, 6.4, 150, 24),
            new Crop(9, "Cantaloupe", 34, CropType.Colorful, 100, 7.6, 50, 11),
            new Crop(10, "Capsicum", 40, CropType.Colorful, 60, 2, 120, 35),
            new Crop(11, "Carrots", 41, CropType.Colorful, 75, 7.8, 100, 21),
            new Crop(12, "Cassava", 159, CropType.Starch, 240, 4, 80, 37),
            new Crop(13, "Cauliflower", 25, CropType.Colorful, 75, 1.2, 120, 20),
            new Crop(14, "Celery", 13, CropType.Colorful, 90, 5.1, 100, 13),
            new Crop(15, "Chard", 30, CropType.Green, 50, 1, 10, 36),
            new Crop(16, "Cherry tomatoes", 18, CropType.Colorful, 73, 15, 100, 40),
            new Crop(17, "Collards", 30, CropType.Green, 60, 1.1, 75, 29),
            new Crop(18, "Corn", 86, CropType.Starch, 75, 7.1, 17, 19),
            new Crop(19, "Coriander", 23, CropType.Green, 75, 1, 25, 36),
            new Crop(20, "Cucumbers", 15, CropType.Colorful, 65, 4, 120, 25),
            new Crop(21, "Dry beans", 341, CropType.Colorful, 60, 5, 17, 28),
            new Crop(34, "Eggplant", 29.5, CropType.Colorful, 75, 0.8, 75, 18),
            new Crop(35, "Garlic", 10, CropType.Colorful, 100, 1.6, 10, 9),
            new Crop(36, "Green beans", 31, CropType.Colorful, 100, 1.5, 30, 8),
            new Crop(37, "Green onions", 32, CropType.Green, 60, 4, 100, 27),
            new Crop(38, "Jute plant", 37, CropType.Green, 120, 3, 28, 39),
            new Crop(39, "Kale", 50, CropType.Green, 55, 0.5, 75, 33),
            new Crop(40, "Kohlrabi", 25, CropType.Green, 55, 0.5, 50, 32),
            new Crop(41, "Leek", 70, CropType.Colorful, 75, 1, 75, 17),
            new Crop(42, "Lettuce", 14, CropType.Green, 33, 7, 135, 39),
            new Crop(43, "Melons", 40, CropType.Colorful, 86, 1, 50, 14),
            new Crop(44, "Oats", 384, CropType.Starch, 120, 3.3, 3, 3),
            new Crop(45, "Okra", 10, CropType.Green, 56, 0.3, 10, 31),
            new Crop(46, "Onions", 42, CropType.Colorful, 115, 17.9, 100, 5),
            new Crop(47, "Peas", 46, CropType.Colorful, 52, 1, 25, 34),
            new Crop(48, "Peppers", 30, CropType.Colorful, 70, 9.2, 120, 23),
            new Crop(49, "Potatoes", 110, CropType.Starch, 110, 32, 100, 7),
            new Crop(50, "Pumpkins", 80, CropType.Colorful, 115, 4.2, 300, 4),
            new Crop(51, "Radish", 63 / 2.2, CropType.Colorful, 22, 0.4, 200, 40),
            new Crop(52, "Spiderherb", 100, CropType.Green, 60, 1.5, 21, 32),
            new Crop(53, "Spinach", 20, CropType.Green, 45, 1.4, 40, 38),
            new Crop(54, "Squash", 20, CropType.Colorful, 58, 4, 200, 30),
            new Crop(55, "Sunhemp", 100, CropType.Starch, 60, 4.5, 19, 30),
            new Crop(56, "Sweet potatoes", 100, CropType.Starch, 110, 6, 100, 6),
            new Crop(57, "Tomatoes", 18, CropType.Colorful, 73, 17.2, 100, 22),
            new Crop(58, "Turnips", 22, CropType.Starch, 50, 0.3, 50, 35),
            new Crop(59, "Watermelons", 30, CropType.Colorful, 75, 13.6, 50, 16),
            // TODO: number of days for wheat is missing
            new Crop(60, "Wheat", 342, CropType.Starch, 1, 3.1, 4, 1),
            new Crop(61, "Zucchini", 80, CropType.Starch, 60, 1, 200, 26)
    ));

    private static void prepopulateDatabase(AppDatabase db) {
        db.cropDao().insertAll(CROPS.toArray(new Crop[0]));
    }
}
