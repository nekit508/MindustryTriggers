package io;

import arc.struct.ObjectMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;
import world.Trigger;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class TriggersChunk implements SaveFileReader.CustomChunk {
    public TriggersChunk(String name) {
        SaveVersion.addCustomChunk(name, this);
    }

    @Override
    public void write(DataOutput stream) throws IOException {
        Writes writes = new Writes(stream);

        writes.l(Trigger.globalTriggerIDCounter);

        writes.l(Trigger.triggersObjects.size());
        Trigger.triggersObjects.forEach((i, t) -> {
            writes.str(t.trigger.name);
            t.save(writes);
        });
    }

    @Override
    public void read(DataInput stream) throws IOException {
        Trigger.globalTriggerIDCounter = 0;
        Trigger.triggersObjects.clear();

        Reads reads = new Reads(stream);

        Trigger.globalTriggerIDCounter = reads.l();

        long triggersLength = reads.l();
        for (long i = 0; i < triggersLength; i++) {
            Trigger.TriggerObject obj = Trigger.triggersTypes.get(reads.str()).createTrigger();
            obj.load(reads);
            obj.add();
        }
    }
}
